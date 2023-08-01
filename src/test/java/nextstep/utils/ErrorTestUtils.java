package nextstep.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.common.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorTestUtils {
    private ErrorTestUtils() {
    }

    public static void 예외_메세지_검증(ExtractableResponse<Response> response, ErrorCode errorCode) {
        assertThat(response.jsonPath().getString("message")).isEqualTo(errorCode.getMessage());
    }
}
