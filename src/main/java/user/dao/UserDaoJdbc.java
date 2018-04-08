package user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import sun.util.logging.PlatformLogger;
import user.domain.Level;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDaoJdbc implements UserDao{

    private DataSource dataSource;
    private JdbcContext jdbcContext; // 인터페이스를 사용하지 않은 DI
    private JdbcTemplate jdbcTemplate; // Spring에서 제공하는 기능(템플릿 메소드 패턴을 쉽게)

    //RowMapper 분리
    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User(rs.getString("id")
                    , rs.getString("name")
                    , rs.getString("password")
                    , Level.valueOf(rs.getInt("level"))
                    , rs.getInt("login")
                    , rs.getInt("recommend"));
            return user;
        }
    };

    public UserDaoJdbc() {

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user) {
        /*
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement
                        ("INSERT INTO user(id, name, password) VALUES (?, ?, ?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
        // 그냥 JdbcTemplete을 활용하면 아래와 같이 간단하게 처리 가능.
        */

        this.jdbcTemplate.update("INSERT INTO user(id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)"
                ,user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM user WHERE id = ?"
                , new Object[]{id}
                , this.userRowMapper);
        /*Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT * FROM user where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }


        rs.close();
        ps.close();
        c.close();

        if(user == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return user;*/
    }

    public void deleteAll() {
        //this.jdbcContext.excuteSql("DELETE FROM user");
        this.jdbcTemplate.update("DELETE FROM user");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM user", Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM user ORDER BY id"
                , userRowMapper);
    }

    public void update(User user) {
        this.jdbcTemplate.update
                ("UPDATE user SET name=?, password=?, level=?, login=?, recommend=? WHERE id=?"
                        , user.getName(), user.getPassword(), user.getLevel().intValue()
                        , user.getLogin(), user.getRecommend(), user.getId());
    }
    /* 미사용
    * 전략패턴에 따라 구현된 오브젝트를 파라미터로, 쿼리를 일괄 실행해준다.
    * 변하는 부분과 변하지 않는 부분을 잘 발라내자!*/
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = stmt.makePreparedStatement(c)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    /*
        미사용.
        템플릿-콜백 패턴에서 콜백 부분을 따로 분리한 모습.
        호출하는 쪽에서 분리하는 것 보다는, 템플릿쪽에 구현해주는 것이 좋다.
        Spring에서는 JdbcTemplete이 이와 같은 패턴으로 구현되어 있다.
     */
    private void excuteSql(final String query) throws SQLException {
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }

}
