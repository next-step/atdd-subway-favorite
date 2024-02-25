package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FailIssueAccessTokenException extends RuntimeException {

    public FailIssueAccessTokenException() {
        super("토큰 발급에 실패하였습니다.");
    }

}


