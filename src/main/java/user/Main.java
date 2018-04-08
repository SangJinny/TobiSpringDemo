package user;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.dao.UserDaoJdbc;
import user.domain.User;

import java.sql.SQLException;

public class Main {

    public static void main (String [] args) throws ClassNotFoundException, SQLException {
        //ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDaoJdbc dao = context.getBean("userDao", UserDaoJdbc.class);
        User user = new User();
        user.setId("aabcc");
        user.setName("전상진3");
        user.setPassword("pwdpwd");

        dao.add(user);
        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());

    }
}
