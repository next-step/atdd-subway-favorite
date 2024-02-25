package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteApiRequester {

    public static ExtractableResponse<Response> createFavorite(String token, long source, long target) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(new FavoriteCreateRequest(source, target))
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> createFavoriteWithSuccess(String token, long source, long target) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(new FavoriteCreateRequest(source, target))
            .when().post("/favorites")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    public static ExtractableResponse<Response> getFavorites(String token) {
        return RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteFavorite(String token, String path) {
        return RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().delete(path)
            .then().log().all()
            .extract();
    }
}
