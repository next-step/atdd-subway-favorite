package nextstep.favorite.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FavoriteControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(FavoriteControllerAdvice.class);

    @ExceptionHandler(FavoriteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String pathExceptionAdvice(FavoriteException e) {
        log.debug("FavoriteException 발생 ::: {}", e.getMessage());
        return e.getMessage();
    }
}