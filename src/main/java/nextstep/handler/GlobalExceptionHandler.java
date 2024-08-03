package nextstep.handler;

import nextstep.common.constant.ErrorCode;
import nextstep.common.dto.ErrorResponse;
import nextstep.favorite.exception.FavoriteException;
import nextstep.line.exception.LineNotFoundException;
import nextstep.member.exception.UnAuthorizedException;
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

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ErrorResponse> LineNotFoundExceptionHandler(LineNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> StationNotFoundExceptionHandler(StationNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StationException.class)
    public ResponseEntity<ErrorResponse> StationExceptionHandler(StationException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<ErrorResponse> SectionExceptionHandler(SectionException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<ErrorResponse> PathExceptionHandler(PathException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> UnAuthorizedExceptionHandler(UnAuthorizedException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FavoriteException.class)
    public ResponseEntity<ErrorResponse> FavoriteExceptionHandler(FavoriteException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
