package nextstep.common.exception.utils;

import nextstep.common.exception.code.ErrorCode;
import nextstep.common.exception.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;

public class ExceptionHandlerUtils {

    private ExceptionHandlerUtils() {
    }

    public static ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, Exception e) {
        return ResponseEntity.status(errorCode.status())
                .body(new ErrorResponse(errorCode.code(), e.getMessage()));
    }
}
