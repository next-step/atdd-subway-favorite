package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String source, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }
}
