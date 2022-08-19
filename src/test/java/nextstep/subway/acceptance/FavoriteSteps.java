package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.auth;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(sourceId));
        params.put("target", String.valueOf(targetId));
        return auth(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorite")
                .then().log().all().extract();
    }
    
}
