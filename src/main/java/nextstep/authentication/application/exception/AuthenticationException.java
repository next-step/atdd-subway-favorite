package nextstep.authentication.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    private static final String MESSAGE = "인증에 실패하였습니다.";

    public AuthenticationException() {
        super(MESSAGE);
    }
}
