package nextstep.common.api;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.RestAssuredHelper;

import java.util.Map;

public class FavoriteApiHelper {

    public static final String FAVORITE_API_PATH = "/favorites";

    private FavoriteApiHelper() {
    }

    public static ExtractableResponse<Response> addFavorite(final String accessToken, final Long sourceId, final Long targetId) {
        return RestAssuredHelper.postWithAuth(FAVORITE_API_PATH, accessToken, Map.of("source", sourceId, "target", targetId));
    }

    public static ExtractableResponse<Response> fetchFavorites(final String accessToken) {
        return RestAssuredHelper.getWithAuth(FAVORITE_API_PATH, accessToken);
    }

    public static ExtractableResponse<Response> removeFavorite(final String accessToken, final Long favoriteId) {
        return RestAssuredHelper.deleteByIdWithAuth(FAVORITE_API_PATH, accessToken, favoriteId);
    }
}
