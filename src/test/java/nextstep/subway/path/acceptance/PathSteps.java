package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {
    public static final String PATH = "/paths";

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_최단_경로_요청(Long source, Long target) {
        String endPoint = PATH + String.format("?source=%d&target=%d", source, target);
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().get(endPoint)
                .then()
                .log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_최단_경로_요청_실패됨(ExtractableResponse<Response> response1) {
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 에러_응답_메세지_비교(ExtractableResponse<Response> response, String message) {
        assertThat(response.asString()).isEqualTo(message);
    }
}
