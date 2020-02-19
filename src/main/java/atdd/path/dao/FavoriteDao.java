package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.FavoritePath;
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

    public FavoriteStation saveForStation(FavoriteStation favoriteStation) {
        final Map<String, Long> params = Map.ofEntries(
                Map.entry("member_id", favoriteStation.getMember().getId()),
                Map.entry("station_id", favoriteStation.getStation().getId())
        );

        final Long favoriteId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return findFavoriteStationById(favoriteId).orElseThrow(NoDataException::new);
    }

    public FavoritePath saveForPath(FavoritePath favoritePath) {
        return null;
    }

    private Optional<FavoriteStation> findFavoriteStationById(Long favoriteId) {
        final String sql = "select fs.id, " +
                "m.id as member_id, " +
                "m.email, " +
                "m.name, " +
                "s.id as station_id, " +
                "s.name as station_name " +
                "from favorite_station fs " +
                "inner join member m on fs.member_id = m.id " +
                "inner join station s on fs.station_id = s.id " +
                "where fs.id = ?";

        final List<FavoriteStation> results = jdbcTemplate.query(
                sql,
                new Object[]{favoriteId},
                mapper);

        return ofNullable(CollectionUtils.isEmpty(results) ? null : results.get(0));
    }

    public List<FavoriteStation> findForStations(Member member) {
        final String sql = "select fs.id, " +
                "m.id as member_id, " +
                "m.email, " +
                "m.name, " +
                "s.id as station_id, " +
                "s.name as station_name " +
                "from favorite_station fs " +
                "inner join member m on fs.member_id = m.id " +
                "inner join station s on fs.station_id = s.id " +
                "where fs.member_id = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{member.getId()},
                mapper);
    }

    private static RowMapper<FavoriteStation> mapper = (rs, rowNum) -> {
        Station findStation = new Station(
                rs.getLong("STATION_ID"), rs.getString("STATION_NAME"));

        Member findMember = new Member(rs.getLong("MEMBER_ID"),
                rs.getString("EMAIL"),
                rs.getString("NAME"), "");

        return new FavoriteStation(rs.getLong("ID"), findMember, findStation);
    };

    public void deleteForStationById(Long favoriteId) {
        final FavoriteStation findFavorite = findFavoriteStationById(favoriteId).orElseThrow(NoDataException::new);
        jdbcTemplate.update("delete from favorite_station where id = ?", findFavorite.getId());
    }

}
