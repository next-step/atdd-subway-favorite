package nextstep.auth.secured;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RoleAuthenticationException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "권한이 없습니다";

    public RoleAuthenticationException() {
        super(DEFAULT_MESSAGE);
    }
}
