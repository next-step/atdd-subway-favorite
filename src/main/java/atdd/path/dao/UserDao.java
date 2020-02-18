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
                .withTableName("STATION")
                .usingGeneratedKeyColumns("ID");
    }

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", user.getId());
        parameters.put("NAME", user.getName());

        Long stationId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(stationId);
    }

    public User findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.start_time as start_time, L.end_time as end_time, L.interval_time as interval_time, " +
                "S.id as station_id, S.name as station_name\n" +
                "from STATION S \n" +
                "left outer join EDGE E on E.source_station_id = S.id or E.target_station_id = S.id \n" +
                "left outer join LINE L on E.line_id = L.id\n" +
                "WHERE S.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapUser(result);
    }

    private User mapUser(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }

        return User.builder()
                .id((Long)result.get(0).get("USER_ID"))
                .email((String)result.get(0).get("USER_EMAIL"))
                .name((String) result.get(0).get("USER_NAME"))
                .build();
    }

    private boolean hasLine(List<Map<String, Object>> result) {
        return result.size() == 0 || result.get(0).get("LINE_ID") == null;
    }

}
