package nextstep.favorite.acceptance;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.fixture.MockTokenFactory;
import org.apache.http.HttpHeaders;

import static io.restassured.RestAssured.given;

public class FavoriteApiRequester {

    private static final String URL = "/favorites";

    public static ExtractableResponse<Response> createFavoriteApiCall(FavoriteRequest request) {
        String accessToken = MockTokenFactory.getAccessToken();
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post(URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findFavoritesApiCall() {
        String accessToken = MockTokenFactory.getAccessToken();
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .when().get(URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteFavoriteApiCall(Long id) {
        String accessToken = MockTokenFactory.getAccessToken();
        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .pathParam("id", id)
                .when().delete(URL + "/{id}")
                .then().log().all()
                .extract();
    }
}
