package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.MemberData;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.subway.utils.SecurityUtil;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return SecurityUtil.given()
                .when().contentType(MediaType.APPLICATION_JSON_VALUE).body(params)
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청_토큰따로(String name, String token) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().contentType(MediaType.APPLICATION_JSON_VALUE).body(params)
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
