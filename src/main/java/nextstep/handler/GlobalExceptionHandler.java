package nextstep.handler;

import nextstep.common.constant.ErrorCode;
import nextstep.common.dto.ErrorResponse;
import nextstep.favorite.exception.FavoriteException;
import nextstep.line.exception.LineNotFoundException;
import nextstep.auth.exception.ApiCallException;
import nextstep.auth.exception.UnAuthorizedException;
import nextstep.member.exception.MemberException;
import nextstep.path.exception.PathException;
import nextstep.section.exception.SectionException;
import nextstep.station.exception.StationException;
import nextstep.station.exception.StationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            LineNotFoundException.class,
            StationNotFoundException.class,
            StationException.class,
            SectionException.class,
            PathException.class,
            FavoriteException.class,
            MemberException.class,
            ApiCallException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> unAuthorizedExceptionHandler(UnAuthorizedException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}

