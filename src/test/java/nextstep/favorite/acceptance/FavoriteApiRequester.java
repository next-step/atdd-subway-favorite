package nextstep.favorite.acceptance;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.fixture.MockTokenFactory;

import static io.restassured.RestAssured.given;

public class FavoriteApiRequester {

    ;


    public static ExtractableResponse<Response> createFavoriteApiCall(FavoriteRequest request) {
        String accessToken = MockTokenFactory.getAccessToken();
        return given().log().all()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findFavoritesApiCall() {
        String accessToken = MockTokenFactory.getAccessToken();
        return given().log().all()
                .header("Authorization", accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteFavoriteApiCall(Long id) {
        String accessToken = MockTokenFactory.getAccessToken();
        return given().log().all()
                .header("Authorization", accessToken)
                .pathParam("id", id)
                .when().delete("/favorites/{id}")
                .then().log().all()
                .extract();
    }
}
