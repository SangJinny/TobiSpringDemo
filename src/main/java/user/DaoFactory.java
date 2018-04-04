package user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.dao.ConnectionMaker;
import user.dao.DConnectionMaker;
import user.dao.UserDao;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
