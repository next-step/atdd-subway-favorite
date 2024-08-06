package nextstep.subway.acceptance.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteUtils {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    public static ExtractableResponse<Response> 즐겨찾기_생성(Long source, Long target, String token) {
        Map<String, Object> params = createParams(source, target);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .header("authorization", "Bearer " + token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록조회(String token) {
        return RestAssured.given().log().all()
            .header("authorization", "Bearer " + token)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String location, String token) {
        return RestAssured.given().log().all()
            .header("authorization", "Bearer " + token)
            .when().delete(location)
            .then().log().all()
            .extract();
    }

    private static Map<String, Object> createParams(Long source, Long target) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return params;
    }
}
