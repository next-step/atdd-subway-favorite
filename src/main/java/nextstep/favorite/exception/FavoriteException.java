package nextstep.favorite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FavoriteException extends RuntimeException {
    public FavoriteException(String message) {
        super(message);
    }
}
