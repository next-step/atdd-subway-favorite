package nextstep.station.ui;

import nextstep.common.exception.code.ErrorCode;
import nextstep.common.exception.dto.ErrorResponse;
import nextstep.common.exception.utils.ExceptionHandlerUtils;
import nextstep.station.domain.exception.CantDeleteStationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StationExceptionHandler {

    @ExceptionHandler(CantDeleteStationException.class)
    public ResponseEntity<ErrorResponse> handleCantDeleteStationException(CantDeleteStationException e) {
        ErrorCode errorCode = StationErrorCode.CANT_DELETE_STATION;
        return ExceptionHandlerUtils.buildErrorResponse(errorCode, e);
    }
}
