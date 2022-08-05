package nextstep.common.exception;

import org.springframework.http.ResponseEntity;

public class ExceptionHandlerUtils {

    private ExceptionHandlerUtils() {
    }

    public static ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, Exception e) {
        return ResponseEntity.status(errorCode.status())
                .body(new ErrorResponse(errorCode.code(), e.getMessage()));
    }
}
