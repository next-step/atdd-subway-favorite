package nextstep.configuration.error;

import nextstep.base.exception.BaseDomainException;
import nextstep.subway.domain.exception.SubwayDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ BaseDomainException.class })
    protected  ResponseEntity<ErrorResponse> handleSubwayException(final BaseDomainException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ErrorResponse.fromBaseException(exception));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.fromSubwayException(new SubwayDomainException()));
    }
}
