package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.AuthSteps.given;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_저장_요청(String accessToken, Long source,
        Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given(accessToken)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return given(accessToken)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return given(accessToken)
            .when().delete(uri)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_안하고_즐겨찾기_조회_요청() {
        return given("")
            .when().get("/favorites")
            .then().log().all().extract();
    }

}
