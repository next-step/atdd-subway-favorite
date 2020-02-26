package atdd.path.fixture;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.domain.Favorite;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static atdd.path.TestConstant.TEST_STATION;
import static atdd.path.dao.FavoriteDao.*;
import static atdd.path.fixture.UserFixture.NEW_USER;

public class FavoriteFixture {
    public static final Long FAVORITE_ID = 0L;
    public static final String STATION_NAME = "강남역";
    public static final Long STATION_ID = 0L;
    public static final Favorite NEW_FAVORITE = new Favorite(NEW_USER, TEST_STATION);
    public static final FavoriteCreateRequestView NEW_FAVORITE_CREATE_VIEW = new FavoriteCreateRequestView(NEW_USER, TEST_STATION);
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
