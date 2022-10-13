package nextstep.common.exception;

import lombok.extern.slf4j.Slf4j;
import nextstep.common.CommonResponse;
import nextstep.common.exception.code.AuthCode;
import nextstep.common.exception.code.CommonCode;
import nextstep.common.exception.code.ResponseCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 비즈니스 로직 exception 처리
     */
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        CommonResponse<Object> response = new CommonResponse<>(responseCode);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 인증 exception 처리
     */
    @ExceptionHandler(value = {AuthException.class})
    public ResponseEntity<Object> handleAuthException(AuthException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        CommonResponse<Object> response = new CommonResponse<>(responseCode);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 파라미터 유효성관련 exception 처리
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> fieldErrorMessage = getFieldErrorMessage(e.getFieldErrors());
        CommonResponse<Object> response = new CommonResponse<>(CommonCode.PARAM_INVALID, fieldErrorMessage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    /**
     * 알 수 없는 exception 처리
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("--- 알 수 없는 오류 감지.  ", ex);
        return ResponseEntity.ok(new CommonResponse<>(CommonCode.ETC));
    }

    private List<String> getFieldErrorMessage(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return errors;
    }
}
