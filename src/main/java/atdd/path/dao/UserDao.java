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
        parameters.put("NAME", user.getName());
        parameters.put("EMAIL", user.getEmail());
        parameters.put("PASSWORD", user.getPassword());

        Long userId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(userId);
    }

    public User findById(Long id) {
        String sql = "SELECT id, name, email \n" +
                "FROM USER \n" +
                "WHERE id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, id);
        return mapUser(result);
    }

    User mapUser(List<Map<String, Object>> result) {
        checkFindResultIsEmpty(result);

        return makeUserByFindData(result.get(0));
    }

    void checkFindResultIsEmpty(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }
    }

    User makeUserByFindData(Map<String, Object> user) {
        return User.builder()
                .id((Long)user.get("ID"))
                .email((String)user.get("EMAIL"))
                .name((String) user.get("NAME"))
                .build();
    }

    private boolean hasLine(List<Map<String, Object>> result) {
        return result.size() == 0 || result.get(0).get("LINE_ID") == null;
    }

}
