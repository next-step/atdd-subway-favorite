package atdd.path.dao;

import atdd.exception.NoDataException;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("STATION")
                .usingGeneratedKeyColumns("ID");
    }

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Station save(Station station) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", station.getId());
        parameters.put("NAME", station.getName());

        Long stationId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(stationId);
    }

    public Station findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.start_time as start_time, L.end_time as end_time, L.interval_time as interval_time, " +
                "S.id as station_id, S.name as station_name\n" +
                "from STATION S \n" +
                "left outer join EDGE E on E.source_station_id = S.id or E.target_station_id = S.id \n" +
                "left outer join LINE L on E.line_id = L.id\n" +
                "WHERE S.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapStation(result);
    }

    public List<Station> findAll() {
        return jdbcTemplate.query(
                "select * from STATION",
                (rs, rowNum) ->
                        new Station(
                                rs.getLong("id"),
                                rs.getString("name")
                        )
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from STATION where id = ?", id);
    }

    private Station mapStation(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }

        return new Station(
                (Long) result.get(0).get("STATION_ID"),
                (String) result.get(0).get("STATION_NAME"),
                extractLines(result)
        );
    }

    private List<Line> extractLines(List<Map<String, Object>> result) {
        if (hasLine(result)) {
            return Collections.EMPTY_LIST;
        }

        return result.stream()
                .map(it ->
                        new Line(
                                (Long) result.get(0).get("LINE_ID"),
                                (String) result.get(0).get("LINE_NAME")
                        ))
                .collect(Collectors.toList());
    }

    private boolean hasLine(List<Map<String, Object>> result) {
        return result.size() == 0 || result.get(0).get("LINE_ID") == null;
    }

}
