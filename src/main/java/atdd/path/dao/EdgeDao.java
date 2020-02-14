package atdd.path.dao;

import atdd.path.domain.Edge;
import atdd.path.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EdgeDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("EDGE")
                .usingGeneratedKeyColumns("ID");
    }

    public EdgeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Edge save(Long lineId, Edge edge) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("LINE_ID", lineId);
        parameters.put("DISTANCE", edge.getDistance());
        parameters.put("SOURCE_STATION_ID", edge.getSourceStation().getId());
        parameters.put("TARGET_STATION_ID", edge.getTargetStation().getId());

        Long edgeId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(edgeId);
    }

    public Edge findById(Long id) {
        Map<String, Object> result = jdbcTemplate.queryForMap(
                "select E.id as id, L.id as line_id, L.name as line_name, " +
                        "SS.id as source_station_id, SS.name as source_station_name, " +
                        "ST.id as target_station_id, ST.name as target_station_name, " +
                        "E.distance " +
                        "from EDGE E " +
                        "join STATION SS ON E.source_station_id = SS.id " +
                        "join STATION ST ON E.target_station_id = ST.id " +
                        "join LINE L on E.line_id = L.id " +
                        "where E.id = ?",
                new Object[]{id}
        );

        return new Edge(
                (Long) result.get("ID"),
                new Station((Long) result.get("SOURCE_STATION_ID"), (String) result.get("SOURCE_STATION_NAME")),
                new Station((Long) result.get("TARGET_STATION_ID"), (String) result.get("TARGET_STATION_NAME")),
                (int) result.get("DISTANCE")
        );
    }

    public void deleteByStationId(Long id) {
        jdbcTemplate.update("delete from EDGE where source_station_id = ? or target_station_id = ?", id, id);
    }
}
