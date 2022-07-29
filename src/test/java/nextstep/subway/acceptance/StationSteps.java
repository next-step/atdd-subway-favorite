package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.utils.RestAssuredUtils.given;

public class StationSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String accessToken) {
        Map<String, String> params = Map.of("name", name);
        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(ExtractableResponse<Response> response, String accessToken) {
        return given(accessToken)
                .when()
                .delete(response.header("location"))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
