package nextstep.subway.configuration;

import nextstep.subway.domain.exception.SubwayDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ SubwayDomainException.class })
    protected  ResponseEntity<ErrorResponse> handleSubwayException(final SubwayDomainException exception) {
        return ResponseEntity.status(exception.getExceptionType().getStatus())
                .body(ErrorResponse.fromSubwayException(exception));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.fromSubwayException(new SubwayDomainException()));
    }
}
