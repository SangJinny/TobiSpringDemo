package user;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.dao.ConnectionMaker;
import user.dao.DConnectionMaker;
import user.dao.UserDao;
import user.domain.User;

import java.sql.SQLException;

public class Main {

    public static void main (String [] args) throws ClassNotFoundException, SQLException {
        //ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);
        User user = new User();
        user.setId("aabcc");
        user.setName("전상진3");
        user.setPassword("pwdpwd");

        dao.add(user);
        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());

    }
}
