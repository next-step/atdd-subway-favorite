package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source,
        Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all().extract();
    }

}
