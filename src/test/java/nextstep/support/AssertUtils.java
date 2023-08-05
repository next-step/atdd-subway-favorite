package nextstep.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    /**
     * <pre>
     * 에러메시지에 대한 검증
     * </pre>
     *
     * @param response
     * @param errorCode
     */
    public static void assertThatErrorMessage(ExtractableResponse<Response> response, ErrorCode errorCode) {
        assertThat(response.jsonPath().getList(ERROR_MESSAGES_KEY, String.class))
                .containsAnyOf(errorCode.getMessage());
    }

    /**
     * <pre>
     * 비로그인 시 발생하는 예외 사항에 대한 검증
     * </pre>
     *
     * @param response
     */
    public static void assertThatNonLoggedIn(ExtractableResponse<Response> response) {
        assertAll(
                () -> AssertUtils.assertThatStatusCode(response, HttpStatus.UNAUTHORIZED),
                () -> AssertUtils.assertThatErrorMessage(response, ErrorCode.AUTHORIZATION_HEADER_IS_BLANK)
        );
    }

}
