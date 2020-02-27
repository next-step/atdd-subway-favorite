package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
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

@Repository
public class FavoriteDao {
    public static final String ID_KEY = "ID";
    public static final int FIRST_INDEX = 0;
    public static final String FAVORITE_TABLE_NAME = "FAVORITE";
    public static final String STATION_ID_KEY = "STATION_ID";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String STATION_NAME_KEY = "STATION_NAME";
    public static final String USER_NAME_KEY = "USER_NAME";
    public static final String USER_PASSWORD_KEY = "USER_PASSWORD";
    public static final String USER_EMAIL_KEY = "USER_EMAIL";
    public static final String FAVORITE_ID_KEY = "FAVORITE_ID";

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

    public Favorite save(Favorite favorite) {
        Long favoriteId = simpleJdbcInsert
                .executeAndReturnKey(Favorite.getSaveParameterByFavorite(favorite))
                .longValue();

        return findById(favoriteId);
    }

    public Favorite findById(Long id) {
        String sql = "SELECT F.id as favorite_id, S.id as station_id, S.name as station_name," +
                "U.id as user_id, U.email as user_email, U.name as user_name, U.password as user_password \n" +
                "FROM FAVORITE F \n" +
                "JOIN STATION S ON F.station_id = S.id \n" +
                "JOIN USER U ON F.user_id = U.id \n" +
                "WHERE F.id  = ?";

        return mapFavorite(jdbcTemplate.queryForList(sql, id));
    }

    Favorite mapFavorite(List<Map<String, Object>> result) {
        checkFindResultIsEmpty(result);

        Map<String, Object> firstRow = result.get(FIRST_INDEX);
        return new Favorite(
                (Long) firstRow.get(FAVORITE_ID_KEY),
                new User((Long) firstRow.get(USER_ID_KEY), (String) firstRow.get(USER_EMAIL_KEY),
                        (String) firstRow.get(USER_NAME_KEY), (String) firstRow.get(USER_PASSWORD_KEY)),
                new Station((Long) firstRow.get(STATION_ID_KEY), (String) firstRow.get(STATION_NAME_KEY))
        );
    }

    void checkFindResultIsEmpty(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }
    }
}
