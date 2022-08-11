package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    private static final String FAVORITE_PATH = "/favorites";
    public static ExtractableResponse<Response> 즐겨찾기_등록(Long source, Long target, String token) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");
        return BearerRestAssured.given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITE_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_단일_조회(String path, String token) {
        return BearerRestAssured.given(token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .when().get(path)
                                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회(String token) {
        return BearerRestAssured.given(token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .when().get(FAVORITE_PATH)
                                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String path, String token) {
        return BearerRestAssured.given(token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .when().delete(path)
                                .then().log().all().extract();
    }

}
