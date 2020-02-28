package atdd.path.fixture;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.domain.Favorite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static atdd.path.TestConstant.*;
import static atdd.path.dao.FavoriteDao.*;
import static atdd.path.fixture.UserFixture.*;

public class FavoriteFixture {
    public static final Long FAVORITE_ID = 0L;
    public static final String STATION_NAME = "강남역";
    public static final Long STATION_ID = 0L;
    public static final Favorite NEW_FAVORITE = new Favorite(NEW_USER, TEST_STATION);
    public static final Favorite NEW_SECOND_FAVORITE = new Favorite(NEW_USER, TEST_STATION_2);
    public static final List<Favorite> NEW_FAVORITES = Arrays.asList(NEW_FAVORITE, NEW_SECOND_FAVORITE);
    public static final String STATION_TYPE = "station";
    public static final FavoriteCreateRequestView NEW_FAVORITE_CREATE_VIEW = new FavoriteCreateRequestView(STATION_ID, STATION_NAME, STATION_TYPE);
    public static List<Map<String, Object>> getDaoFavorites() {
        return Arrays.asList(getDaoFavorite(), getDaoSecondFavorite());
    }

    public static Map<String, Object> getDaoFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(STATION_ID_KEY, STATION_ID);
        savedFavorite.put(STATION_NAME_KEY, STATION_NAME);
        savedFavorite.put(USER_ID_KEY, KIM_ID);
        savedFavorite.put(USER_NAME_KEY, KIM_NAME);
        savedFavorite.put(USER_EMAIL_KEY, KIM_EMAIL);
        return savedFavorite;
    }

    public static Map<String, Object> getDaoSecondFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(STATION_ID_KEY, STATION_ID_2);
        savedFavorite.put(STATION_NAME_KEY, STATION_NAME_2);
        savedFavorite.put(USER_ID_KEY, KIM_ID);
        savedFavorite.put(USER_NAME_KEY, KIM_NAME);
        savedFavorite.put(USER_EMAIL_KEY, KIM_EMAIL);
        return savedFavorite;
    }

}
