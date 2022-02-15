package nextstep.error;

import nextstep.exception.AuthenticationException;
import nextstep.exception.DuplicateException;
import nextstep.exception.NextStepException;
import nextstep.exception.NotConnectedException;
import nextstep.exception.NotExistAuthorizationException;
import nextstep.exception.NotFoundLineException;
import nextstep.exception.NotFoundMemberException;
import nextstep.exception.NotFoundStationException;
import nextstep.exception.SameStationException;
import nextstep.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authentication(final AuthenticationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(NotExistAuthorizationException.class)
    public ResponseEntity<ErrorResponse> notExistAuthorization(final NotExistAuthorizationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ErrorResponse> notFoundMember(final NotFoundMemberException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(NotConnectedException.class)
    public ResponseEntity<ErrorResponse> notConnected(final NotConnectedException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(SameStationException.class)
    public ResponseEntity<ErrorResponse> sameStation(final SameStationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(NotFoundStationException.class)
    public ResponseEntity<ErrorResponse> notFoundStation(final NotFoundStationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(NotFoundLineException.class)
    public ResponseEntity<ErrorResponse> notFoundLine(final NotFoundLineException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> duplicate(final DuplicateException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validation(final ValidationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getErrors()));
    }

    @ExceptionHandler(NextStepException.class)
    public ResponseEntity<ErrorResponse> nextStep(final NextStepException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(final Exception e) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
