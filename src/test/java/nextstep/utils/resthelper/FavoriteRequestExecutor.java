package nextstep.utils.resthelper;

import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.api.favorite.application.model.dto.FavoriteCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/12
 */
public class FavoriteRequestExecutor extends AbstractRequestExecutor {

	private static final String FAVORITES_URL_PATH = "/favorites";

	private static RequestSpecification getRequestSpecification() {
		return given().log().all().contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> executeCreateFavoriteRequest(String authorizationToken, FavoriteCreateRequest createRequest) {
		return doPost(getRequestSpecification().header("Authorization", authorizationToken), FAVORITES_URL_PATH, createRequest);
	}

	public static ExtractableResponse<Response> executeGetFavoritesRequest(String authorizationToken) {
		return doGet(getRequestSpecification().header("Authorization", authorizationToken), FAVORITES_URL_PATH);
	}

	public static ExtractableResponse<Response> executeDeleteFavoriteRequest(String authorizationToken, Long favoriteId) {
		return doDelete(getRequestSpecification().header("Authorization", authorizationToken), FAVORITES_URL_PATH + "/" + favoriteId);
	}
}
