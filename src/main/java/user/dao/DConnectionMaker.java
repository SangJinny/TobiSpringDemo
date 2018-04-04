package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/* 미사용. DataSource를 쓰면 된다. */
public class DConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/study_db", "study_user", "study");
        return c;
    }
}
