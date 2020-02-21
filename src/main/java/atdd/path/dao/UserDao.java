package atdd.path.dao;

import atdd.path.application.dto.FindByEmailResponseView;
import atdd.path.application.exception.NoDataException;
import atdd.path.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {
    public static final String ID_KEY = "ID";
    public static final String NAME_KEY = "NAME";
    public static final String EMAIL_KEY = "EMAIL";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static final int FIRST_INDEX = 0;
    public static final String USER_TABLE_NAME = "USER";

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(USER_TABLE_NAME)
                .usingGeneratedKeyColumns(ID_KEY);
    }

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        Long userId = simpleJdbcInsert
                .executeAndReturnKey(User.getSaveParameterByUser(user))
                .longValue();

        return findById(userId);
    }

    public User findById(Long id) {
        String sql = "SELECT id, name, email \n" +
                "FROM USER \n" +
                "WHERE id = ?";

        return mapUser(jdbcTemplate.queryForList(sql, id));
    }

    public FindByEmailResponseView findByEmail(String email) {
        String sql = "SELECT id, name, email \n" +
                "FROM USER \n" +
                "WHERE email = ?";

        return FindByEmailResponseView
                .toDtoEntity(jdbcTemplate.queryForList(sql, email));
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM USER WHERE id = ?", id);
    }

    User mapUser(List<Map<String, Object>> result) {
        checkFindResultIsEmpty(result);

        return User.getUserByFindData(result.get(FIRST_INDEX));
    }

    void checkFindResultIsEmpty(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }
    }
}
