package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {

    private static final String FAVORITE_PATH = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_구간_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post(FAVORITE_PATH)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 인증_없이_즐겨찾기_구간_생성_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post(FAVORITE_PATH)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_구간_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(FAVORITE_PATH)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 인증_없이_즐겨찾기_구간_목록_조회_요청() {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(FAVORITE_PATH)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_구간_제거_요청(String accessToken, String location) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when().delete(location)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 인증_없이_즐겨찾기_구간_제거_요청(String location) {
        return RestAssured.given().log().all()
            .when().delete(location)
            .then().log().all().extract();
    }
}
