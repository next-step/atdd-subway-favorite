package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotFoundUserException extends RuntimeException {

    public NotFoundUserException() {
        super("유효하지 않은 사용자 정보입니다.");
    }

}
