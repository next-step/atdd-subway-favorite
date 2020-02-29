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
    public static final Long EDGE_FIRST_ID = 1L;
    public static final Long LINE_FIRST_ID = 1L;
    public static final Long EDGE_SECOND_ID = 2L;
    public static final Favorite NEW_FAVORITE = new Favorite(NEW_USER, TEST_STATION);
    public static final Favorite NEW_SECOND_FAVORITE = new Favorite(NEW_USER, TEST_STATION_2);
    public static final List<Favorite> NEW_FAVORITES = Arrays.asList(NEW_FAVORITE, NEW_SECOND_FAVORITE);
    public static final FavoriteCreateRequestView STATION_FAVORITE_CREATE_REQUEST_VIEW
            = new FavoriteCreateRequestView(STATION_ID, STATION_NAME, STATION_TYPE);
    public static final FavoriteCreateRequestView EDGE_FAVORITE_CREATE_REQUEST_VIEW
            = new FavoriteCreateRequestView(LINE_FIRST_ID, LINE_NAME, EDGE_TYPE);

    public static List<Map<String, Object>> getDaoFavorites() {
        return Arrays.asList(getDaoStationFavorite(), getDaoStationSecondFavorite());
    }

    public static List<Map<String, Object>> getDaoEdgeFavorites() {
        return Arrays.asList(getDaoFirstEdgeFavorite(), getDaoSecondEdgeFavorite());
    }

    public static Map<String, Object> getDaoStationFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(STATION_ID_KEY, STATION_ID);
        savedFavorite.put(STATION_NAME_KEY, STATION_NAME);
        savedFavorite.put(USER_ID_KEY, KIM_ID);
        savedFavorite.put(USER_NAME_KEY, KIM_NAME);
        savedFavorite.put(USER_EMAIL_KEY, KIM_EMAIL);
        return savedFavorite;
    }

    public static Map<String, Object> getDaoStationSecondFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(STATION_ID_KEY, STATION_ID_2);
        savedFavorite.put(STATION_NAME_KEY, STATION_NAME_2);
        savedFavorite.put(USER_ID_KEY, KIM_ID);
        savedFavorite.put(USER_NAME_KEY, KIM_NAME);
        savedFavorite.put(USER_EMAIL_KEY, KIM_EMAIL);
        return savedFavorite;
    }

    public static Map<String, Object> getDaoFirstEdgeFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(EDGE_ID_KEY, EDGE_FIRST_ID);
        savedFavorite.put(SOURCE_STATION_ID_KEY, STATION_ID);
        savedFavorite.put(SOURCE_STATION_NAME_KEY, STATION_NAME);
        savedFavorite.put(TARGET_STATION_ID_KEY, STATION_ID_2);
        savedFavorite.put(TARGET_STATION_NAME_KEY, STATION_NAME_2);
        savedFavorite.put(DISTANCE_ID_KEY, 10);
        savedFavorite.put(USER_ID_KEY, KIM_ID);
        savedFavorite.put(USER_NAME_KEY, KIM_NAME);
        savedFavorite.put(USER_EMAIL_KEY, KIM_EMAIL);
        return savedFavorite;
    }

    public static Map<String, Object> getDaoSecondEdgeFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(EDGE_ID_KEY, EDGE_SECOND_ID);
        savedFavorite.put(SOURCE_STATION_ID_KEY, STATION_ID_5);
        savedFavorite.put(SOURCE_STATION_NAME_KEY, STATION_NAME_5);
        savedFavorite.put(TARGET_STATION_ID_KEY, STATION_ID_6);
        savedFavorite.put(TARGET_STATION_NAME_KEY, STATION_NAME_6);
        savedFavorite.put(DISTANCE_ID_KEY, 20);
        savedFavorite.put(USER_ID_KEY, KIM_ID);
        savedFavorite.put(USER_NAME_KEY, KIM_NAME);
        savedFavorite.put(USER_EMAIL_KEY, KIM_EMAIL);
        return savedFavorite;
    }
}
