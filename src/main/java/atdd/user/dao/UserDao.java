package atdd.user.dao;

import atdd.user.domain.User;
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

    @Autowired
    public void setDataSource(final DataSource dataSource){
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");
    }

    public UserDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", user.getId());
        parameters.put("NAME", user.getName());
        parameters.put("PASSWORD", user.getPassword());
        parameters.put("EMAIL", user.getEmail());

        Long userId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(userId);
    }

    public User findById(Long id){
        Map<String, Object> result = jdbcTemplate.queryForMap(
                "select *  from USERS where ID = ?", id
        );

        return new User(
                (Long)result.get("ID"),
                (String) result.get("NAME"),
                (String) result.get("PASSWORD"),
                (String) result.get("EMAIL")
        );
    }

    public User findByEmail(String email){
        Map<String, Object> result = jdbcTemplate.queryForMap(
                "select * from USERS where EMAIL = ?", email
        );
        return new User(
                (Long)result.get("ID"),
                (String) result.get("NAME"),
                (String) result.get("PASSWORD"),
                (String) result.get("EMAIL")
        );
    }

    public void deleteByUserId(Long id){
        jdbcTemplate.update("delete from USERS where ID = ?", id);
    }
}
