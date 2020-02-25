package atdd.path.fixture;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static atdd.path.dao.FavoriteDao.*;

public class FavoriteFixture {
    public static final Long FAVORITE_ID = 0L;
    public static final String STATION_NAME = "강남역";
    public static final Long STATION_ID = 0L;
    public static List<Map<String, Object>> getDaoFavorites() {
        return Collections.singletonList(getDaoFavorite());
    }

    public static Map<String, Object> getDaoFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(STATION_ID_KEY, STATION_ID);
        savedFavorite.put(STATION_NAME_KEY, STATION_NAME);
        return savedFavorite;
    }
}
