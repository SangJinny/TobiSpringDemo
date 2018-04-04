package user;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.dao.UserDao;
import user.domain.User;

import java.sql.SQLException;
/*
    1. applicationContext가 없이 테스트하는 상황을 만드는 것이 가장 좋다.
    2. 필요한경우 아래처럼 DI를 이용하자.
 */
/* Junit을 편리하게 쓰기위해 Spring에서 제공하는 기능. */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;
    private UserDao dao;

    @Before
    public void setUp() {
        dao = context.getBean("userDao", UserDao.class);
    }

    @Test
    public void addAndGet() throws SQLException{
        User user1 = new User("1", "전상진1", "pwd1");
        User user2 = new User("2", "전상진2", "pwd2");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));


        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User tempUser = dao.get(user1.getId());
        assertThat(tempUser.getName(), is(user1.getName()));
    }

    @Test
    public void count() throws SQLException {
        User user1 = new User("1", "상진1", "password1");
        User user2 = new User("2", "상진2", "password2");
        User user3 = new User("3", "상진3", "password3");

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
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("asfdlkjsdakljasfljkasf");
    }


}
