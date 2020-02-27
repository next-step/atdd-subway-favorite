package atdd.path.fixture;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.domain.Favorite;

import java.util.*;

import static atdd.path.TestConstant.TEST_STATION;
import static atdd.path.TestConstant.TEST_STATION_2;
import static atdd.path.dao.FavoriteDao.*;
import static atdd.path.fixture.UserFixture.*;

public class FavoriteFixture {
    public static final Long FAVORITE_ID = 0L;
    public static final String STATION_NAME = "강남역";
    public static final Long STATION_ID = 0L;
    public static final Favorite NEW_FAVORITE = new Favorite(NEW_USER, TEST_STATION);
    public static final Favorite NEW_SECOND_FAVORITE = new Favorite(NEW_USER, TEST_STATION_2);
    public static final List<Favorite> NEW_FAVORITES = Arrays.asList(NEW_FAVORITE, NEW_SECOND_FAVORITE);
    public static final FavoriteCreateRequestView NEW_FAVORITE_CREATE_VIEW = new FavoriteCreateRequestView(KIM_ID, STATION_ID, STATION_NAME);
    public static List<Map<String, Object>> getDaoFavorites() {
        return Collections.singletonList(getDaoFavorite());
    }

    public static Map<String, Object> getDaoFavorite() {
        Map<String, Object> savedFavorite = new HashMap<>();
        savedFavorite.put(FAVORITE_ID_KEY, FAVORITE_ID);
        savedFavorite.put(STATION_ID_KEY, STATION_ID);
        savedFavorite.put(STATION_NAME_KEY, STATION_NAME);
        savedFavorite.put(USER_ID_KEY, KIM_ID);
        savedFavorite.put(USER_NAME_KEY, KIM_NAME);
        savedFavorite.put(USER_EMAIL_KEY, KIM_EMAIL);
        savedFavorite.put(USER_PASSWORD_KEY, KIM_PASSWORD);
        return savedFavorite;
    }
}
