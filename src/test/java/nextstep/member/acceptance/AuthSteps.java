package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.utils.AcceptanceTest;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class AuthSteps extends AcceptanceTest {

    public static ExtractableResponse<Response> 토큰_획득(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 깃헙_로그인_요청(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 유저_정보_조회(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}
