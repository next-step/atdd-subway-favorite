package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.givenAdminRole;

public class StationSteps {
    public static ExtractableResponse<Response> 관리자_권한으로_지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return givenAdminRole()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
