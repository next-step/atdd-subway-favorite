package nextstep.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super("로그인 되어있지 않습니다.");
    }
}
