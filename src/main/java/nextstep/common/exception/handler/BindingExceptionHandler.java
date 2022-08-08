package nextstep.common.exception.handler;

import nextstep.common.exception.code.CommonErrorCode;
import nextstep.common.exception.dto.BindingErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class BindingExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BindingErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        CommonErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        List<BindingErrorResponse.ValidationError> validationErrors = toValidtaionErrors(e);
        BindingErrorResponse bindingErrorResponse = new BindingErrorResponse(errorCode.name(), validationErrors);
        return ResponseEntity.status(errorCode.status())
                .body(bindingErrorResponse);
    }

    private List<BindingErrorResponse.ValidationError> toValidtaionErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(it -> new BindingErrorResponse.ValidationError(it.getField(), it.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}
