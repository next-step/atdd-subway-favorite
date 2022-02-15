package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class FavoritesSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));
        params.put("accessToken", accessToken);

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    public static Long getFavoritesId(ExtractableResponse<Response> response) {
        String[] split = response.header("Location").split("/");
        return Long.valueOf(split[split.length - 1]);
    }
}