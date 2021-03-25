package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Map<String, String> param, TokenResponse loginResponse) {
        String uri = "/favorites";
        return RestAssured.given().log().all()
                .auth().oauth2(loginResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(TokenResponse loginResponse) {
        String uri = "/favorites";
        return RestAssured.given().log().all()
                .auth().oauth2(loginResponse.getAccessToken())
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, TokenResponse tokenResponse) {
        String uri = response.header("Location");
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
