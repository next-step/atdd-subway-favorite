package nextstep.member.application.exception;

import lombok.extern.slf4j.Slf4j;
import nextstep.common.exception.ErrorCode;
import nextstep.member.application.dto.MemberErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<MemberErrorResponse> handleInvalidTokenException(InvalidTokenException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.info("인증예외 이유: {} 코드: {}", errorCode.getMessage(), errorCode.getCode());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MemberErrorResponse.of(errorCode));
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<MemberErrorResponse> handleNotFoundMemberException(NotFoundMemberException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        loggingMemberException(errorCode);

        return ResponseEntity.badRequest()
                .body(MemberErrorResponse.of(errorCode));
    }

    @ExceptionHandler(GithubOauthConnectionException.class)
    public ResponseEntity<MemberErrorResponse> handleGithubOauthConnectionException(
            GithubOauthConnectionException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        loggingMemberException(errorCode);

        return ResponseEntity.internalServerError()
                .body(MemberErrorResponse.of(errorCode));
    }

    private void loggingMemberException(ErrorCode errorCode) {
        log.info("회원 예외 발생 메세지: {}, 코드: {}", errorCode.getMessage(), errorCode.getCode());
    }
}
