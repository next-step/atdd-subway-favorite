package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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

    public User save(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("EMAIL", user.getEmail());
        params.put("NAME", user.getName());
        params.put("PASSWORD", user.getPassword());

        Long userId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return findById(userId);
    }

    public User findById(Long id) {
        String sql = "SELECT id, email, name FROM USER WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
            new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("name")
            ));
    }
}
