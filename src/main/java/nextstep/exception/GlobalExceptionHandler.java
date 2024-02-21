package nextstep.exception;

import nextstep.FailResponse;
import nextstep.favorite.CannotFavoriteNonexistentPathException;
import nextstep.auth.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {InvalidInputException.class})
    protected ResponseEntity<FailResponse> handleInvalidInputException(InvalidInputException ex) {
        return ResponseEntity.badRequest().body(new FailResponse(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {PathNotFoundException.class})
    protected ResponseEntity<FailResponse> handlePathNotFoundException(PathNotFoundException ex) {
        return ResponseEntity.badRequest().body(new FailResponse(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<FailResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailResponse(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {UnauthorizedDeletionException.class})
    protected ResponseEntity<FailResponse> handleUnauthorizedDeletionException(UnauthorizedDeletionException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailResponse(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {CannotFavoriteNonexistentPathException.class})
    protected ResponseEntity<FailResponse> handleCannotFavoriteNonexistentPathException(CannotFavoriteNonexistentPathException ex) {
        return ResponseEntity.badRequest().body(new FailResponse(ex.getMessage()));
    }
}
