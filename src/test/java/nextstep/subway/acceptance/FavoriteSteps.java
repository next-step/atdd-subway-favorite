package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(long source, long target,
        String accessToken) {

        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("authorization", "Bearer " + accessToken)
            .body(params)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("authorization", "Bearer " + accessToken)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }
}
