package user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = stmt.makePreparedStatement(c)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
}
