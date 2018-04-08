package user;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.dao.UserDao;
import user.dao.UserDaoJdbc;
import user.domain.Level;
import user.domain.User;
import user.service.UserService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
    1. applicationContext가 없이 테스트하는 상황을 만드는 것이 가장 좋다.
    2. 필요한경우 아래처럼 DI를 이용하자.
 */
/* Junit을 편리하게 쓰기위해 Spring에서 제공하는 기능. */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoJdbcTest {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    private List<User> userList;
    @Before
    public void setUp() {
        user1 = new User("1", "전상진1", "pwd1", Level.BASIC, 1, 0);
        user2 = new User("2", "전상진2", "pwd2", Level.SILVER, 55, 10);
        user3 = new User("3", "전상진2", "pwd2", Level.GOLD, 100, 40);
        userList = new ArrayList<User>();
        userList.add(new User("4", "전상진4", "pwd4", Level.BASIC, 49, 0));
        userList.add(new User("5", "전상진5", "pwd5", Level.BASIC, 50, 0));
        userList.add(new User("6", "전상진6", "pwd6", Level.SILVER, 60, 29));
        userList.add(new User("7", "전상진7", "pwd7", Level.SILVER, 60, 30));
        userList.add(new User("8", "전상진8", "pwd8", Level.GOLD, 100, 100));
    }

    @Test
    public void addAndGet() {


        dao.deleteAll();
        assertThat(dao.getCount(), is(0));


        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User tempUser = dao.get(user1.getId());
        checkSameUser(tempUser, user1);
    }

    @Test
    public void count()  {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("asfdlkjsdakljasfljkasf");
    }

    @Test
    public void getAllTest() {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));

        List<User> userList = dao.getAll();
        assertThat(userList.get(0).getId(), is("1"));
        assertThat(userList.get(1).getId(), is("2"));
        assertThat(userList.get(2).getId(), is("3"));
    }

    @Test
    public void sqlExceptionTranslateTest(){
        dao.deleteAll();
        try{
            dao.add(user1);
            dao.add(user1);
            dao.add(user1);
        }catch (DuplicateKeyException de) {
            SQLException sqlEx = (SQLException)de.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlEx), instanceOf(DuplicateKeyException.class));
        }
    }

    @Test
    public void update() {
        dao.deleteAll();
        dao.add(user1);

        user1.setName("수정된이름1");
        user1.setLevel(Level.GOLD);

        dao.update(user1);
        User tempUser = dao.get(user1.getId());
        checkSameUser(tempUser, user1);
    }

    @Test
    public void upgradeLevels() {
        dao.deleteAll();
        for(User user: userList) {
            dao.add(user);
        }
        userService.upgradeLevels();
        checkLevel(userList.get(0), Level.BASIC);
        checkLevel(userList.get(1), Level.SILVER);
        checkLevel(userList.get(2), Level.SILVER);
        checkLevel(userList.get(3), Level.GOLD);
        checkLevel(userList.get(4), Level.GOLD);
    }

    @Test
    public void add() {
        User userWithoutLevel = new User("77", "전상진", "PWD", null, 0, 0);
        User userWithLevel = new User("88", "전상진", "PWD", Level.GOLD, 0, 0);

        userService.add(userWithoutLevel);
        userService.add(userWithLevel);

        User tempUser = dao.get(userWithoutLevel.getId());
        assertThat(tempUser.getLevel(), is(Level.BASIC));
        tempUser = dao.get(userWithLevel.getId());
        assertThat(tempUser.getLevel(), is(userWithLevel.getLevel()));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = dao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }
}
