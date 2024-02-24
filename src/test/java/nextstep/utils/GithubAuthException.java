package nextstep.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class GithubAuthException extends RuntimeException {

    public GithubAuthException() {
        super("인증에 실패하였습니다.");
    }

}
