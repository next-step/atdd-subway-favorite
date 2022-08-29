package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.EMAIL;
import static nextstep.subway.acceptance.MemberSteps.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        return 지하철역_생성_요청_토큰_포함(name, 로그인_되어_있음(EMAIL, PASSWORD));
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청_토큰_포함(String name, String token) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
