package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* 미사용.
*  이렇게 하면 Dao의 메소드 마다 클래스가 하나씩 늘어나게 된다.
*  차라리 Dao의 메소드 안에서 익명클래스를 사용하는 것이 낫다.
*  */
public class DeleteAllStatement implements StatementStrategy{

    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("DELETE FROM user");
        return ps;
    }
}
