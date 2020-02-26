package atdd.path.dao;

import atdd.exception.NoDataException;
import atdd.path.domain.FavoritePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FavoritePathDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("FAVORITE_PATH")
                .usingGeneratedKeyColumns("ID");
    }

    public FavoritePathDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FavoritePath save(FavoritePath favoritePath) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("OWNER", favoritePath.getOwner());
        parameters.put("SOURCE_STATION_ID", favoritePath.getSourceStationId());
        parameters.put("TARGET_STATION_ID", favoritePath.getTargetStationId());

        Long favoritePathId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return findById(favoritePathId);
    }

    public List<FavoritePath> findAll(long owner) {
        return jdbcTemplate.query(
                "select * from FAVORITE_PATH WHERE owner = " + owner,
                (rs, rowNum) ->
                        FavoritePath.builder()
                                .id(rs.getLong("id"))
                                .owner(rs.getLong("owner"))
                                .sourceStationId(rs.getLong("source_station_id"))
                                .targetStationId(rs.getLong("target_station_id")).build()
        );
    }

    private FavoritePath findById(Long id) {
        String sql = "select id, owner, source_station_Id, target_station_Id " +
                "from FAVORITE_PATH  \n" +
                "WHERE id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapFavoritePath(result);
    }

    private FavoritePath mapFavoritePath(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }

        return FavoritePath.builder()
                .id((Long) result.get(0).get("ID"))
                .owner((Long) result.get(0).get("OWNER"))
                .sourceStationId((Long) result.get(0).get("SOURCE_STATION_ID"))
                .targetStationId((Long) result.get(0).get("TARGET_STATION_ID")).build();
    }
}
