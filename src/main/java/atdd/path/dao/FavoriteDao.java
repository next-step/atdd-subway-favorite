package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Edge;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FavoriteDao {
    public static final String ID_KEY = "ID";
    public static final int FIRST_INDEX = 0;
    public static final String FAVORITE_TABLE_NAME = "FAVORITE";
    public static final String ITEM_ID_KEY = "ITEM_ID";
    public static final String STATION_ID_KEY = "STATION_ID";
    public static final String EDGE_ID_KEY = "EDGE_ID";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String STATION_NAME_KEY = "STATION_NAME";
    public static final String ITEM_NAME_KEY = "ITEM_NAME";
    public static final String USER_NAME_KEY = "USER_NAME";
    public static final String USER_EMAIL_KEY = "USER_EMAIL";
    public static final String FAVORITE_ID_KEY = "FAVORITE_ID";
    public static final String TYPE_KEY = "TYPE";
    public static final String STATION_TYPE = "station";
    public static final String EDGE_TYPE = "edge";
    public static final String SOURCE_STATION_ID_KEY = "source_station_id";
    public static final String TARGET_STATION_ID_KEY = "target_station_id";
    public static final String SOURCE_STATION_NAME_KEY = "source_station_name";
    public static final String TARGET_STATION_NAME_KEY = "target_station_name";
    public static final String DISTANCE_ID_KEY = "distance";

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(FAVORITE_TABLE_NAME)
                .usingGeneratedKeyColumns(ID_KEY);
    }

    public FavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Favorite save(Favorite favorite, String type) {
        Long favoriteId = simpleJdbcInsert
                .executeAndReturnKey(Favorite.getSaveParameterByFavorite(favorite, type))
                .longValue();

        if (STATION_TYPE.equals(type)) {
            return findStationById(favoriteId, type);
        }

        return findEdgeById(favoriteId, type);
    }

    public Favorite findStationById(Long id, String type) {
        String sql = "SELECT F.id as favorite_id, S.id as station_id, S.name as station_name," +
                "U.id as user_id, U.email as user_email, U.name as user_name \n" +
                "FROM FAVORITE F \n" +
                "JOIN STATION S ON F.item_id = S.id \n" +
                "JOIN USER U ON F.user_id = U.id \n" +
                "WHERE F.id  = ?" +
                "AND F.type = ?";

        return mapFavorite(jdbcTemplate.queryForList(sql, id, type), type);
    }

    public Favorite findEdgeById(Long id, String type) {
        String sql = "SELECT F.id as favorite_id, F.type as type," +
                "E.id as edge_id, E.line_id as line_id," +
                "SS.id as source_station_id, SS.name as source_station_name," +
                "TS.id as target_station_id, TS.name as target_station_name," +
                "E.distance as distance," +
                "U.id as user_id, U.email as user_email, U.name as user_name \n" +
                "FROM FAVORITE F \n" +
                "JOIN EDGE E ON F.item_id = E.id \n" +
                "JOIN STATION SS ON SS.id = E.source_station_id \n" +
                "JOIN STATION TS ON TS.id = E.target_station_id \n" +
                "JOIN USER U ON F.user_id = U.id \n" +
                "WHERE F.id  = ?" +
                "AND F.type = ?";

        return mapFavorite(jdbcTemplate.queryForList(sql, id, type), type);
    }

    Favorite mapFavorite(List<Map<String, Object>> result, String type) {
        if (STATION_TYPE.equals(type)) {
            return mapStationFavorite(result);
        }

        return mapEdgeFavorite(result);
    }


    Favorite mapStationFavorite(List<Map<String, Object>> result) {
        Map<String, Object> firstRow = result.get(FIRST_INDEX);
        return new Favorite(
                (Long) firstRow.get(FAVORITE_ID_KEY),
                new User((Long) firstRow.get(USER_ID_KEY), (String) firstRow.get(USER_EMAIL_KEY),
                        (String) firstRow.get(USER_NAME_KEY)),
                new Station((Long) firstRow.get(STATION_ID_KEY), (String) firstRow.get(STATION_NAME_KEY))
        );
    }

    Favorite mapEdgeFavorite(List<Map<String, Object>> result) {
        Map<String, Object> firstRow = result.get(FIRST_INDEX);
        return new Favorite(
                (Long) firstRow.get(FAVORITE_ID_KEY),
                new User((Long) firstRow.get(USER_ID_KEY), (String) firstRow.get(USER_EMAIL_KEY),
                        (String) firstRow.get(USER_NAME_KEY)),
                new Edge((Long) firstRow.get(EDGE_ID_KEY)
                        , new Station((Long) firstRow.get(SOURCE_STATION_ID_KEY), (String) firstRow.get(SOURCE_STATION_NAME_KEY))
                        , new Station((Long) firstRow.get(TARGET_STATION_ID_KEY), (String) firstRow.get(TARGET_STATION_NAME_KEY))
                        , (int) firstRow.get(DISTANCE_ID_KEY))
        );
    }


    void checkFindResultIsEmpty(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }
    }

    public List<Favorite> findByUser(User user) {
        String sql = "SELECT F.id as favorite_id, S.id as station_id, S.name as station_name \n" +
                "FROM FAVORITE F \n" +
                "JOIN STATION S ON F.item_id = S.id \n" +
                "JOIN USER U ON F.user_id = U.id \n" +
                "WHERE U.id  = ?";

        return mapFavorites(jdbcTemplate.queryForList(sql, user.getId()), user);
    }

    List<Favorite> mapFavorites(List<Map<String, Object>> rows, User user) {
        return rows.stream()
                .map(row -> new Favorite(
                        (Long) row.get(FAVORITE_ID_KEY)
                        , user
                        , new Station((Long) row.get(STATION_ID_KEY), (String) row.get(STATION_NAME_KEY))))
                .collect(Collectors.toList());
    }

    public void deleteStation(User user, Long stationId) {
        String sql = "DELETE FROM FAVORITE WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, stationId, user.getId());
    }
}
