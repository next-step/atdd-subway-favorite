package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static String BASE_PATH = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(
        TokenResponse tokenResponse,
        FavoriteRequest favoriteRequest
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .body(favoriteRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(BASE_PATH)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(
        TokenResponse tokenResponse
    ) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get(BASE_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(
        TokenResponse tokenResponse,
        ExtractableResponse<Response> createResponse
    ) {
        String uri = createResponse.header("Location");
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete(uri)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 비회원_즐겨찾기_생성_요청(
        FavoriteRequest favoriteRequest
    ) {
        return RestAssured
            .given().log().all()
            .body(favoriteRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(BASE_PATH)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 비회원_즐겨찾기_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get(BASE_PATH)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 비회원_즐겨찾기_삭제_요청(
        ExtractableResponse<Response> createResponse
    ) {
        String uri = createResponse.header("Location");
        return RestAssured
            .given().log().all()
            .when().delete(uri)
            .then().log().all().extract();
    }
}
