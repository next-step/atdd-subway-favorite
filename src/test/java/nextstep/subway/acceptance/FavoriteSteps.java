package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class FavoriteSteps {

    private static final String ROOT_PATH = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(long source, long target, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");
        return RestGivenWithOauth2.from(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(ROOT_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(ROOT_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(long id, String accessToken) {
        return RestGivenWithOauth2.from(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(ROOT_PATH + "/" + id)
                .then().log().all().extract();
    }
}