package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceId, Long targetId) {
        Map<String, Long> params = new HashMap<>();
        params.put("sourceId", sourceId);
        params.put("targetId", targetId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }
}
