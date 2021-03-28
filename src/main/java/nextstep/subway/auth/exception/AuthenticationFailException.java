package nextstep.subway.auth.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException() {
        super("인증이 올바르지 않습니다.");
    }
}
