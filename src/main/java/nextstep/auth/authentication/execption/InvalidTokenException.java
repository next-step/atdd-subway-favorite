package nextstep.auth.authentication.execption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends AuthenticationException {
    public static final String INVALID_TOKEN = "올바르지 않은 토큰입니다.";

    public InvalidTokenException(String message) {
        super(message);
    }
}
