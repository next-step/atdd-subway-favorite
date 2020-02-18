package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Member;
import atdd.path.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
public class FavoriteDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public FavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("FAVORITE_STATION")
                .usingGeneratedKeyColumns("ID");
    }

    public FavoriteStation saveForStation(Member member, Station station) {
        final Map<String, Long> params = Map.ofEntries(
                Map.entry("member_id", member.getId()),
                Map.entry("station_id", station.getId())
        );

        final Long favoriteId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return findFavoriteStationById(favoriteId).orElseThrow(NoDataException::new);
    }

    private Optional<FavoriteStation> findFavoriteStationById(Long favoriteId) {
        final String sql = "select fs.id, s.id as station_id, s.name as station_name " +
                "from favorite_station fs " +
                "inner join station s on fs.station_id = s.id " +
                "where fs.id = ?";

        final List<FavoriteStation> results = jdbcTemplate.query(
                sql,
                new Object[]{favoriteId},
                mapper);

        return ofNullable(CollectionUtils.isEmpty(results) ? null : results.get(0));
    }

    private static RowMapper<FavoriteStation> mapper = (rs, rowNum) -> {
        Station findStation = new Station(
                rs.getLong("STATION_ID"), rs.getString("STATION_NAME"));

        return new FavoriteStation(rs.getLong("ID"), findStation);
    };

}
