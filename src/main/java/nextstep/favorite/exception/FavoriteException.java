package nextstep.favorite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FavoriteException extends RuntimeException {
    public FavoriteException(String message) {
        super(message);
    }
}
