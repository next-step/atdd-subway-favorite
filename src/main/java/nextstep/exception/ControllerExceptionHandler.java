package nextstep.exception;

import io.jsonwebtoken.MalformedJwtException;
import nextstep.exception.favoriteException.FavoriteException;
import nextstep.exception.newsectionexception.NewSectionException;
import nextstep.exception.notfoundexception.NotFoundException;
import nextstep.exception.pathexception.PathException;
import nextstep.exception.removesectionexception.RemoveSectionException;
import nextstep.subway.ui.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgsException(DataIntegrityViolationException e) {
        return getResponseEntity(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return getResponseEntity(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        return getResponseEntity(e.getMessage());
    }

    @ExceptionHandler(NewSectionException.class)
    public ResponseEntity<ExceptionResponse> handleNewSectionException(NewSectionException e) {
        return getResponseEntity(e.getMessage());
    }

    @ExceptionHandler(RemoveSectionException.class)
    public ResponseEntity<ExceptionResponse> handleRemoveSectionException(RemoveSectionException e) {
        return getResponseEntity(e.getMessage());
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<ExceptionResponse> handleRemoveSectionException(PathException e) {
        return getResponseEntity(e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ExceptionResponse> handleMalformedJwtException(MalformedJwtException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FavoriteException.class)
    public ResponseEntity<ExceptionResponse> handleFavoriteException(FavoriteException e) {
        return getResponseEntity(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(String e) {
        return new ResponseEntity<>(new ExceptionResponse(e), HttpStatus.BAD_REQUEST);
    }

}
