package atdd.path.dao;

import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FavoriteDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public FavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("FAVORITE_STATION")
                .usingGeneratedKeyColumns("ID");
    }

    @Transactional(rollbackFor = Exception.class)
    public FavoriteStation saveFavoriteStation(Long stationId, User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("USER_ID", user.getId());
        parameters.put("STATION_ID", stationId);

        Long favoriteId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findFavoriteStationById(favoriteId);
    }

    public List<FavoriteStation> findFavoriteStationsByUserId(Long userId) {
        String sql = "select fs.id, " +
                "u.id as user_id, u.email, u.name, " +
                "s.id as station_id, s.name as station_name " +
                "from FAVORITE_STATION as fs " +
                "inner join USER u on fs.user_id = u.id " +
                "inner join STATION s on fs.station_id = s.id " +
                "where u.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{userId});
        return mapFavoriteStations(result);
    }

    private List<FavoriteStation> mapFavoriteStations(List<Map<String, Object>> result) {
        if(result.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return result.stream().map(r ->
                new FavoriteStation(
                        (Long) result.get(0).get("ID"),
                        new User((Long) result.get(0).get("USER_ID"), (String) result.get(0).get("email"),
                                (String) result.get(0).get("name")),
                        new Station((Long) result.get(0).get("STATION_ID"), (String) result.get(0).get("STATION_NAME")))
                ).collect(Collectors.toList());
    }

    public FavoriteStation findFavoriteStationById(Long id) {
        Map<String, Object> result = jdbcTemplate.queryForMap(
                "select fs.id, u.id as user_id, " +
                        "u.email, u.name, " +
                        "s.id as station_id, " +
                        "s.name as station_name " +
                        "from FAVORITE_STATION fs " +
                        "inner join USER u on fs.user_id = u.id " +
                        "inner join STATION s on fs.station_id = s.id " +
                        "where fs.id = ?",
                new Object[]{id}
        );

        return mapFavoriteStation(result);
    }

    private FavoriteStation mapFavoriteStation(Map<String, Object> result) {
        return new FavoriteStation(
                (Long) result.get("ID"),
                new User((Long) result.get("USER_ID"), (String) result.get("email"), (String) result.get("name")),
                new Station((Long) result.get("STATION_ID"), (String) result.get("STATION_NAME"))
        );
    }

    public void deleteFavoriteStationById(Long id) {
        jdbcTemplate.update("delete from FAVORITE_STATION where id = ?", id);
    }
}
