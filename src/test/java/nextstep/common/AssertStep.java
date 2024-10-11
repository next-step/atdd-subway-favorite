package nextstep.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AssertStep {

    public static void 에러코드400을_검증한다(ExtractableResponse<Response> response, RuntimeException e) {
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getString("message")).isEqualTo(e.getMessage());
    }
}
