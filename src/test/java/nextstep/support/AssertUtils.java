package nextstep.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertUtils {

    private static final String ERROR_MESSAGES_KEY = "errorMessages";

    /**
     * <pre>
     * 응답코드에 대한 검증
     * </pre>
     *
     * @param response
     * @param status
     */
    public static void assertThatStatusCode(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

}
