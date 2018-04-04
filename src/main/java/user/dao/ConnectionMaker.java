package user.dao;

import java.sql.Connection;
import java.sql.SQLException;
/* 미사용. DataSource를 쓰면 된다. */
public interface ConnectionMaker {
    public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
