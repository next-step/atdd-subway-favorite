package nextstep.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("인증되지 않은 사용자입니다.");
    }
}
