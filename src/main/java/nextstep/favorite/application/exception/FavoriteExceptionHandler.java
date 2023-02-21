package nextstep.favorite.application.exception;

import lombok.extern.slf4j.Slf4j;
import nextstep.common.exception.ErrorCode;
import nextstep.favorite.application.dto.FavoriteErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FavoriteExceptionHandler {

    @ExceptionHandler(NotFoundFavoriteException.class)
    public ResponseEntity<FavoriteErrorResponse> handleNotFoundFavoriteException(NotFoundFavoriteException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.info("즐겨찾기 예외 이유: {} 코드: {}", errorCode.getMessage(), errorCode.getCode());

        return ResponseEntity.badRequest()
                .body(FavoriteErrorResponse.of(errorCode));
    }

    @ExceptionHandler(InvalidFavoriteRemoveRequest.class)
    public ResponseEntity<FavoriteErrorResponse> handleInvalidFavoriteRemoveRequest(
            InvalidFavoriteRemoveRequest exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.info("즐겨찾기 예외 이유: {} 코드: {}", errorCode.getMessage(), errorCode.getCode());

        return ResponseEntity.badRequest()
                .body(FavoriteErrorResponse.of(errorCode));
    }
}
