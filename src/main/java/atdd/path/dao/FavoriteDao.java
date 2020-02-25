package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Favorite;
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
    public static final String STATION_ID_KEY = "station_id";

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
        Long userId = simpleJdbcInsert
                .executeAndReturnKey(Favorite.getSaveParameterByFavorite(favorite))
                .longValue();

        return findById(userId);
    }

    public Favorite findById(Long id) {
        String sql = "SELECT F.id as favorite_id, S.id as station_id, S.name as station_name \n" +
                "FROM favorite F \n" +
                "JOIN STATION S ON F.station_id = S.id \n" +
                "WHERE F.id  = ?";

        return mapFavorite(jdbcTemplate.queryForList(sql, id));
    }

    Favorite mapFavorite(List<Map<String, Object>> result) {
        checkFindResultIsEmpty(result);

        return new Favorite(
                (Long) result.get(0).get("")
        );
    }

    void checkFindResultIsEmpty(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }
    }
}
