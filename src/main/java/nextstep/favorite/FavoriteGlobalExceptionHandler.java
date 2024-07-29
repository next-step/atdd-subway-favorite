package nextstep.favorite;

import nextstep.common.ErrorResponse;
import nextstep.favorite.exception.NoFavoriteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FavoriteGlobalExceptionHandler {

    @ExceptionHandler(NoFavoriteException.class)
    public ResponseEntity<String> lineException(Exception exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }
}
