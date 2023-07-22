package nextstep.api.subway.acceptance;

import org.springframework.http.HttpStatus;

import io.restassured.response.ValidatableResponse;
import nextstep.api.ExceptionResponse;

public class AcceptanceHelper {

    public static ExceptionResponse asExceptionResponse(final ValidatableResponse response) {
        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
        return response.extract()
                .jsonPath()
                .getObject("", ExceptionResponse.class);
    }

    public static void statusCodeShouldBe(final ValidatableResponse response, final HttpStatus expected) {
        response.statusCode(expected.value());
    }
}
