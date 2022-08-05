package nextstep.favorite.exception;

import nextstep.common.ErrorResponse;
import nextstep.favorite.ui.FavoriteController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {FavoriteController.class})
public class FavoriteExceptionHandler {

    @ExceptionHandler({NotFoundFavoriteException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception e) {
        return ResponseEntity.noContent().build();
    }
}
