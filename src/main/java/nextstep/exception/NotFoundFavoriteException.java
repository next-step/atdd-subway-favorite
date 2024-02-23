package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundFavoriteException extends RuntimeException {

    public NotFoundFavoriteException() {
        super("없는 즐겨찾기입니다.");
    }

}
