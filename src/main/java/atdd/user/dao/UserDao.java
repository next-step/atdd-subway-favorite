package atdd.user.dao;

import atdd.user.domain.Email;
import atdd.user.domain.User;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(User.TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public User create(User user) {
        final Number id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user));
        return User.of(id.longValue(), new Email(user.getEmail()), user.getName(), user.getPassword());
    }

}
