package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.RestAssuredAssist.given;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 로그인_상태에서_즐겨찾기_추가_요청(String accessToken, Long sourceId, Long targetId) {
        Map<String, String> params = createParams(sourceId, targetId);
        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그아웃_상태에서_즐겨찾기_추가(Long sourceId, Long targetId) {
        Map<String, String> params = createParams(sourceId, targetId);
        return given().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return given(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_상태에서_즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return given(accessToken)
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그아웃_상태에서_즐겨찾기_삭제_요청(Long favoriteId) {
        return given()
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> createParams(Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("sourceId", "" + sourceId);
        params.put("targetId", "" + targetId);
        return params;
    }
}
