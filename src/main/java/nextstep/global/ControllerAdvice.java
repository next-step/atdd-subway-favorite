package nextstep.global;

import nextstep.subway.line.exception.SectionAddFailureException;
import nextstep.subway.line.exception.SectionDeleteFailureException;
import nextstep.subway.path.exception.PathException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({
        SectionAddFailureException.class, SectionDeleteFailureException.class, PathException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSectionAddFailureException(RuntimeException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }
}
