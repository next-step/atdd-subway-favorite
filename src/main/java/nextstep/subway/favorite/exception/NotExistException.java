package nextstep.subway.favorite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistException extends RuntimeException {
    public NotExistException() {
        super("존재하지 않는 id에 대한 요청입니다.");
    }
}
