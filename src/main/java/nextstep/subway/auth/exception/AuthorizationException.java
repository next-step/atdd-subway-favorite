package nextstep.subway.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("권한이 없는 요청입니다.");
    }

}
