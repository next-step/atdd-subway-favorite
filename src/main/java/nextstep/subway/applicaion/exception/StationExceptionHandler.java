package nextstep.subway.applicaion.exception;

import lombok.extern.slf4j.Slf4j;
import nextstep.common.exception.ErrorCode;
import nextstep.subway.applicaion.dto.SubwayErrorCodeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class StationExceptionHandler {

    @ExceptionHandler(NotFoundStationException.class)
    public ResponseEntity<SubwayErrorCodeResponse> handleNotFoundStationException(NotFoundStationException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.info("지하철 노선 관련 예외 이유: {} 코드: {}", errorCode.getMessage(), errorCode.getCode());

        return ResponseEntity.badRequest().body(SubwayErrorCodeResponse.of(errorCode));
    }
}
