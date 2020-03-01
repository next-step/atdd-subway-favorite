package atdd.path.dao;

import atdd.exception.NoDataException;
import atdd.path.domain.Edge;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("LINE")
                .usingGeneratedKeyColumns("ID");
    }

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Line save(Line line) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", line.getName());
        parameters.put("START_TIME", line.getStartTime());
        parameters.put("END_TIME", line.getEndTime());
        parameters.put("INTERVAL_TIME", line.getInterval());

        Long lineId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(lineId);
    }

    public Line findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.start_time as start_time, L.end_time as end_time, L.interval_time as interval_time, " +
                "E.id as edge_id, E.distance as distance, " +
                "S.id as source_station_id, S.name as source_station_name\n," +
                "ST.id as target_station_id, ST.name as target_station_name\n" +
                "from LINE L \n" +
                "left outer join EDGE E on L.id = E.line_id\n" +
                "left outer join STATION S on E.source_station_id = S.id \n" +
                "left outer join STATION ST on E.target_station_id = ST.id \n" +
                "WHERE L.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapLine(result);
    }

    public List<Line> findAll() {
        String sql = "select L.id as line_id, L.name as line_name, L.start_time as start_time, L.end_time as end_time, L.interval_time as interval_time, " +
                "E.id as edge_id, E.distance as distance, " +
                "S.id as source_station_id, S.name as source_station_name,\n" +
                "ST.id as target_station_id, ST.name as target_station_name\n" +
                "from LINE L \n" +
                "left outer join EDGE E on L.id = E.line_id\n" +
                "left outer join STATION S on E.source_station_id = S.id\n" +
                "left outer join STATION ST on E.target_station_id = ST.id\n";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<Long, List<Map<String, Object>>> resultByLine = result.stream().collect(Collectors.groupingBy(it -> (Long) it.get("line_id")));
        return resultByLine.entrySet().stream()
                .map(it -> mapLine(it.getValue()))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    private Line mapLine(List<Map<String, Object>> result) {
        if (result.size() == 0) {
            throw new NoDataException();
        }

        List<Edge> edges = extractEdges(result);

        return new Line((Long) result.get(0).get("LINE_ID"),
                (String) result.get(0).get("LINE_NAME"),
                edges,
                LocalTime.parse(result.get(0).get("START_TIME").toString()),
                LocalTime.parse(result.get(0).get("END_TIME").toString()),
                (int) result.get(0).get("interval_time"));
    }

    private List<Edge> extractEdges(List<Map<String, Object>> result) {
        if (result.isEmpty() || result.get(0).get("EDGE_ID") == null) {
            return Collections.EMPTY_LIST;
        }
        return result.stream()
                .collect(Collectors.groupingBy(it -> it.get("EDGE_ID")))
                .entrySet()
                .stream()
                .map(it ->
                        new Edge((Long) it.getKey(),
                                new Station((Long) it.getValue().get(0).get("SOURCE_STATION_ID"), (String) it.getValue().get(0).get("SOURCE_STATION_Name")),
                                new Station((Long) it.getValue().get(0).get("TARGET_STATION_ID"), (String) it.getValue().get(0).get("TARGET_STATION_Name")),
                                (int) it.getValue().get(0).get("DISTANCE")))
                .collect(Collectors.toList());
    }

}
