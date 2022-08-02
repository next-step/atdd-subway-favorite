package nextstep.favorite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidFavoriteOwnerException extends RuntimeException {
    public InvalidFavoriteOwnerException() {
        super("invalid favorite id to other owner");
    }
}
