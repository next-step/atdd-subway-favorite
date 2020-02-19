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
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
public class FavoriteDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert stationSimpleJdbcInsert;
    private SimpleJdbcInsert pathSimpleJdbcInsert;

    public FavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.stationSimpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("FAVORITE_STATION")
                .usingGeneratedKeyColumns("ID");

        this.pathSimpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("FAVORITE_PATH")
                .usingGeneratedKeyColumns("ID");
    }

    public FavoriteStation saveForStation(FavoriteStation favoriteStation) {
        final Map<String, Long> params = Map.ofEntries(
                Map.entry("member_id", favoriteStation.getMember().getId()),
                Map.entry("station_id", favoriteStation.getStation().getId())
        );

        final Long favoriteId = stationSimpleJdbcInsert.executeAndReturnKey(params).longValue();
        return findFavoriteStationById(favoriteId).orElseThrow(NoDataException::new);
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
                stationRowMapper);

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
                stationRowMapper);
    }

    private static RowMapper<FavoriteStation> stationRowMapper = (rs, rowNum) ->
            new FavoriteStation(rs.getLong("ID"), memberOf(rs), stationOf(rs, ""));

    public void deleteForStationById(Long favoriteId) {
        final FavoriteStation findFavorite = findFavoriteStationById(favoriteId).orElseThrow(NoDataException::new);
        jdbcTemplate.update("delete from favorite_station where id = ?", findFavorite.getId());
    }

    public FavoritePath saveForPath(FavoritePath favoritePath) {
        final Map<String, Long> params = Map.ofEntries(
                Map.entry("member_id", favoritePath.getMember().getId()),
                Map.entry("source_station_id", favoritePath.getSourceStation().getId()),
                Map.entry("target_station_id", favoritePath.getTargetStation().getId())
        );

        final Long favoriteId = pathSimpleJdbcInsert.executeAndReturnKey(params).longValue();
        return findFavoritePathById(favoriteId).orElseThrow(NoDataException::new);
    }

    private Optional<FavoritePath> findFavoritePathById(Long favoriteId) {
        final String sql = "select fp.id, " +
                "m.id as member_id, " +
                "m.email, " +
                "m.name, " +
                "s1.id as source_station_id,   " +
                "s1.name as source_station_name,  " +
                "s2.id as target_station_id,   " +
                "s2.name as target_station_name  " +
                "from favorite_path fp " +
                "inner join member m on fp.member_id = m.id " +
                "inner join station s1 on fp.source_station_id = s1.id " +
                "inner join station s2 on fp.target_station_id = s2.id " +
                "where fp.id = ?";

        final List<FavoritePath> results = jdbcTemplate.query(sql,
                new Object[]{favoriteId},
                pathRowMapper);

        return ofNullable(CollectionUtils.isEmpty(results) ? null : results.get(0));
    }

    private static RowMapper<FavoritePath> pathRowMapper = (rs, rowNum) ->
            new FavoritePath(rs.getLong("ID"),
                    memberOf(rs),
                    stationOf(rs, "SOURCE"),
                    stationOf(rs, "TARGET"));

    private static Station stationOf(ResultSet rs, String prefix) throws SQLException {
        final String newPrefix = StringUtils.isEmpty(prefix) ? "" : prefix + "_";
        return new Station(rs.getLong(newPrefix + "STATION_ID"),
                rs.getString(newPrefix +"STATION_NAME"));
    }

    private static Member memberOf(ResultSet rs) throws SQLException {
        return new Member(rs.getLong("MEMBER_ID"),
                rs.getString("EMAIL"),
                rs.getString("NAME"),
                "");
    }

    public List<FavoritePath> findFavoritePath(Member member) {
        return null;
    }
}
