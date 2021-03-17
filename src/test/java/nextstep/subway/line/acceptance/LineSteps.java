package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(TokenResponse user, LineRequest lineRequest) {
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(user, lineRequest);
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return lineResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(TokenResponse user, LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(TokenResponse user) {
        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(TokenResponse user, LineResponse response) {
        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", response.getId())
                .when().get("/lines/{lineId}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(TokenResponse user, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(TokenResponse user, ExtractableResponse<Response> response, LineRequest lineRequest) {
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().put(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(TokenResponse user, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .when().delete(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(TokenResponse user, LineResponse line, StationResponse upStation, StationResponse downStation, int distance, int duration) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance, duration);

        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .pathParam("lineId", line.getId())
                .when().post("/lines/{lineId}/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(TokenResponse user, LineResponse line, StationResponse station) {
        return RestAssured.given().log().all()
                .auth().oauth2(user.getAccessToken())
                .pathParam("lineId", line.getId())
                .queryParam("stationId", station.getId())
                .when().delete("/lines/{lineId}/sections")
                .then().log().all().extract();
    }
}
