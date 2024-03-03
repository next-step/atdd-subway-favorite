package nextstep.auth.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.MediaType;

public class TokenSteps {

    public static ExtractableResponse<Response> 토근_생성_요청(Map<String, String> 토큰_생성_본문) {
        return RestAssured.given().log().all()
            .body(토큰_생성_본문)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static String 토큰_생성_응답에서_토큰값_추출(ExtractableResponse<Response> 토큰_생성_응답) {
        return 토큰_생성_응답.jsonPath().get("accessToken");
    }
}
