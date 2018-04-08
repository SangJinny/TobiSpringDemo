package user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/*
    JdbcTemplete을 쓰면 된다..
    JdbcTemplete이 돌아가는 구조를 알아보기 위한 예제.
 */
public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /*
        템플릿의 구현체를 받아 처리하는 부분.
     */
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = stmt.makePreparedStatement(c)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /*
        템플릿-콜백 패턴에서 콜백 부분을 따로 분리한 모습.
        호출하는 쪽에서 분리하는 것 보다는, 템플릿쪽에 구현해주는 것이 좋다.
     */
    public void excuteSql(final String query) throws SQLException {
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }
}
