package atdd.path.dao;

import atdd.exception.NoDataException;
import atdd.path.domain.FavoriteStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FavoriteStationDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("FAVORITE_STATION")
                .usingGeneratedKeyColumns("ID");
    }

    public FavoriteStationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FavoriteStation save(FavoriteStation favoriteStation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("OWNER", favoriteStation.getOwner());
        parameters.put("STATION_ID", favoriteStation.getStationId());

        Long favoriteStationId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(favoriteStationId);
    }

    public List<FavoriteStation> findAllByOwner(long owner) {
        String sql = "select id, owner, station_Id " +
                "from FAVORITE_STATION  \n" +
                "WHERE owner = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{owner});

        return mapFavoriteStationList(result);
    }

    public void deleteByIdAndOwner(long id, long owner) {
        jdbcTemplate.update("delete from FAVORITE_STATION where id = ? AND owner = ?", id, owner);
    }

    private FavoriteStation findById(Long id) {
        String sql = "select id, owner, station_Id " +
                "from FAVORITE_STATION  \n" +
                "WHERE id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapFavoriteStation(result);
    }

    private List<FavoriteStation> mapFavoriteStationList(List<Map<String, Object>> result) {
        List<FavoriteStation> favoriteStations = new ArrayList<>();

        for (Map<String, Object> map : result) {
            FavoriteStation favoriteStation = FavoriteStation.builder()
                    .id((Long) map.get("ID"))
                    .owner((Long) map.get("OWNER"))
                    .stationId((Long) map.get("STATION_ID")).build();

            favoriteStations.add(favoriteStation);
        }

        return favoriteStations;
    }

    private FavoriteStation mapFavoriteStation(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoDataException();
        }

        return FavoriteStation.builder()
                .id((Long) result.get(0).get("ID"))
                .owner((Long) result.get(0).get("OWNER"))
                .stationId((Long) result.get(0).get("STATION_ID")).build();
    }
}
