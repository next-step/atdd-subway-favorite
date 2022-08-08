package nextstep.auth.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "인증이 필요합니다";

    public AuthenticationException() {
        super(DEFAULT_MESSAGE);
    }
}
