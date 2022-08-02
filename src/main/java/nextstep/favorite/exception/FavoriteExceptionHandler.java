package nextstep.favorite.exception;

import nextstep.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FavoriteExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundFavoriteException e) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.NOT_FOUND, e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
