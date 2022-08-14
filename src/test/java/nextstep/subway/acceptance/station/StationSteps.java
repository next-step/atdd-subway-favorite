package nextstep.subway.acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.member.MemberSteps.어드민_로그인_되어_있음;
import static nextstep.subway.utils.RestAssuredUtils.securedGiven;

public class StationSteps {

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return securedGiven(어드민_로그인_되어_있음())
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static void 지하철역_제거_요청(String location) {
        securedGiven(어드민_로그인_되어_있음())
                .when()
                .delete(location)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

}
