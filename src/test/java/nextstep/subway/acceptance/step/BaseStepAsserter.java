package nextstep.subway.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseStepAsserter {
    public static void 응답_상태값이_올바른지_검증한다(ExtractableResponse<Response> response, int statusValue) {
        assertThat(response.statusCode()).isEqualTo(statusValue);
    }
}
