package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotMyFavoriteException extends RuntimeException {

    public NotMyFavoriteException(String message) {
        super(message);
    }
}
