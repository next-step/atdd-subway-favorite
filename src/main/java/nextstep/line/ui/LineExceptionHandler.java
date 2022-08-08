package nextstep.line.ui;

import nextstep.common.exception.code.ErrorCode;
import nextstep.common.exception.dto.ErrorResponse;
import nextstep.common.exception.utils.ExceptionHandlerUtils;
import nextstep.line.domain.exception.IllegalSectionOperationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineExceptionHandler {

    @ExceptionHandler(IllegalSectionOperationException.class)
    public ResponseEntity<ErrorResponse> handleIllegalSectionOperationException(IllegalSectionOperationException e) {
        ErrorCode errorCode = LineErrorCode.ILLEGAL_SECTION_OPERATION;
        return ExceptionHandlerUtils.buildErrorResponse(errorCode, e);
    }
}
