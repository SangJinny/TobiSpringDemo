package user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.dao.ConnectionMaker;
import user.dao.DConnectionMaker;
import user.dao.UserDaoJdbc;

@Configuration
public class DaoFactory {

    @Bean
    public UserDaoJdbc userDao() {
        return new UserDaoJdbc();
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
