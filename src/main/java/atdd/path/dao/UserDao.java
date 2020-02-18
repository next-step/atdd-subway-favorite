package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("USER")
                .usingGeneratedKeyColumns("ID");
    }

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", user.getId());
        parameters.put("EMAIL", user.getEmail());
        parameters.put("NAME", user.getName());
        parameters.put("PASSWORD", user.getPassword());

        Long userId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(userId);
    }

    public User findById(Long id) {
        String sql = "select U.id as user_id, " +
                "U.email as user_email, U.name as user_name," +
                "U.password as user_password\n" +
                "from USER U \n" +
                "WHERE U.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapUser(result);
    }

    private User mapUser(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }

        return new User(
                (Long) result.get(0).get("user_id"),
                (String) result.get(0).get("user_email"),
                (String) result.get(0).get("user_name"),
                (String) result.get(0).get("user_password")
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from USER where id = ?", id);
    }
}
