package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 로그인_상태에서_즐겨찾기_추가_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = createParams(source, target);
        return given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그아웃_상태에서_즐겨찾기_추가(Long source, Long target) {
        Map<String, String> params = createParams(source, target);
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_상태에서_즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorite/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그아웃_상태에서_즐겨찾기_삭제_요청(Long favoriteId) {
        return given().log().all()
                .when().delete("/favorite/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> createParams(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", "" + source);
        params.put("target", "" + target);
        return params;
    }
}
