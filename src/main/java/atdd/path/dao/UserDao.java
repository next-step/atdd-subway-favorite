package atdd.path.dao;

import atdd.path.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("USER")
                .usingGeneratedKeyColumns("ID");
    }

    @Transactional(rollbackFor = Exception.class)
    public User save(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("EMAIL", user.getEmail());
        params.put("NAME", user.getName());
        params.put("PASSWORD", user.getPassword());

        Long userId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return findById(userId);
    }

    public User findById(Long id) {
        String sql = "SELECT id, email, name, password " +
                "FROM USER " +
                "WHERE id = ?";

        return getUser(new Object[]{id}, sql);
    }

    public User findByEmail(String email) {
        String sql = "SELECT id, email, name, password " +
                "FROM USER " +
                "WHERE email = ?";

        return getUser(new Object[]{email}, sql);
    }

    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, name, password " +
                "FROM USER " +
                "WHERE email = ?" +
                "AND password = ?";

        return getUser(new Object[]{email, password}, sql);
    }

    private User getUser(Object[] objects, String sql) {
        return jdbcTemplate.queryForObject(sql, objects, (rs, rowNum) ->
                new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password")
                ));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM USER WHERE id = ?", id);
    }
}
