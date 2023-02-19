package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_구간_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 인증_없이_즐겨찾기_구간_생성_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all().extract();
    }
}
