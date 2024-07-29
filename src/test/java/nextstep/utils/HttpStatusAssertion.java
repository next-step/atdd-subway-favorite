package nextstep.utils;

import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpStatusAssertion {

    public static void assertOk(int status) {
        assertThat(status).isEqualTo(HttpStatus.OK.value());
    }

    public static void assertNoContent(int status) {
        assertThat(status).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void assertBadRequest(int status) {
        assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void assertCreated(int status) {
        assertThat(status).isEqualTo(HttpStatus.CREATED.value());
    }
}
