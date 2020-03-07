package atdd.path.dao;

import atdd.path.domain.FavoritePath;
import atdd.path.domain.Station;
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
public class FavoritePathDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public FavoritePathDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("FAVORITE_PATH")
                .usingGeneratedKeyColumns("ID");
    }

    @Transactional(rollbackFor = Exception.class)
    public FavoritePath save(Long userId, Long sourceId, Long targetId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("USER_ID", userId);
        parameters.put("SOURCE_STATION_ID", sourceId);
        parameters.put("TARGET_STATION_ID", targetId);

        Long favoritePathId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findFavoritePathById(favoritePathId);
    }

    public List<FavoritePath> findFavoritePathsByUserId(Long userId) {
        String sql = "select fp.id, u.id as user_id, " +
                "u.email, " +
                "u.name, " +
                "s1.id as source_station_id, " +
                "s1.name as source_station_name, " +
                "s2.id as target_station_id, " +
                "s2.name as target_station_name, " +
                "from FAVORITE_PATH as fp " +
                "inner join USER u on fp.user_id = u.id " +
                "inner join STATION s1 on fp.source_station_id = s1.id " +
                "inner join STATION s2 on fp.target_station_id = s2.id " +
                "where fp.user_id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{userId});
        return mapFavoritePaths(result);
    }

    private List<FavoritePath> mapFavoritePaths(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return result.stream().map(r ->
                new FavoritePath(
                        (Long) r.get("ID"),
                        new Station((Long) r.get("SOURCE_STATION_ID"), (String) r.get("SOURCE_STATION_NAME")),
                        new Station((Long) r.get("TARGET_STATION_ID"), (String) r.get("TARGET_STATION_NAME")))
        ).collect(Collectors.toList());
    }

    public FavoritePath findFavoritePathById(Long id) {
        Map<String, Object> result = jdbcTemplate.queryForMap(
                "select fp.id, u.id as user_id, " +
                        "u.email, " +
                        "u.name, " +
                        "s1.id as source_station_id, " +
                        "s1.name as source_station_name, " +
                        "s2.id as target_station_id, " +
                        "s2.name as target_station_name, " +
                        "from FAVORITE_PATH fp " +
                        "inner join USER u on fp.user_id = u.id " +
                        "inner join STATION s1 on fp.source_station_id = s1.id " +
                        "inner join STATION s2 on fp.target_station_id = s2.id " +
                        "where fp.id = ?",
                new Object[]{id}
        );
        return mapFavoritePath(result);
    }

    private FavoritePath mapFavoritePath(Map<String, Object> result) {
        return new FavoritePath(
                (Long) result.get("ID"),
                new Station((Long) result.get("SOURCE_STATION_ID"), (String) result.get("SOURCE_STATION_NAME")),
                new Station((Long) result.get("TARGET_STATION_ID"), (String) result.get("TARGET_STATION_NAME"))
        );
    }

    public FavoritePath findFavoritePathByIdAndUserId(Long id, Long userId) {
        Map<String, Object> result = jdbcTemplate.queryForMap(
                "select fp.id, u.id as user_id, " +
                        "u.email, " +
                        "u.name, " +
                        "s1.id as source_station_id, " +
                        "s1.name as source_station_name, " +
                        "s2.id as target_station_id, " +
                        "s2.name as target_station_name, " +
                        "from FAVORITE_PATH fp " +
                        "inner join USER u on fp.user_id = u.id " +
                        "inner join STATION s1 on fp.source_station_id = s1.id " +
                        "inner join STATION s2 on fp.target_station_id = s2.id " +
                        "where fp.id = ?" +
                        "and fp.user_id = ?",
                new Object[]{id, userId}
        );
        return mapFavoritePath(result);
    }

    public void deleteFavoritePathById(Long id) {
        jdbcTemplate.update("delete from FAVORITE_PATH where id = ?", id);
    }
}
