package nextstep.common.exception;

import nextstep.favorite.domain.exception.FavoriteConflictException;
import nextstep.favorite.domain.exception.FavoriteMemberException;
import nextstep.favorite.domain.exception.FavoriteNotFoundException;
import nextstep.line.domain.exception.LineNotFoundException;
import nextstep.line.domain.exception.SectionException;
import nextstep.path.domain.exception.PathBadRequestException;
import nextstep.path.domain.exception.PathNotFoundException;
import nextstep.station.domain.exception.StationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController {
    private final Logger logger = LoggerFactory.getLogger("subway.exception.ExceptionController");

    @ExceptionHandler(SectionException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> exception(SectionException e, HttpServletRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage());
        logger.warn("UNPROCESSABLE_ENTITY occurred on {} {}. Message={}",
                req.getMethod(), req.getRequestURI(), e.getMessage()
        );
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler({
            PathNotFoundException.class,
            FavoriteNotFoundException.class,
            StationNotFoundException.class,
            LineNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> notFoundException(RuntimeException e, HttpServletRequest req) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e, req);
    }

    @ExceptionHandler(PathBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> badRequestException(RuntimeException e, HttpServletRequest req) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e, req);
    }

    @ExceptionHandler(FavoriteConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> conflictException(RuntimeException e, HttpServletRequest req) {
        return createErrorResponse(HttpStatus.CONFLICT, e, req);
    }

    @ExceptionHandler(FavoriteMemberException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> forbiddenException(RuntimeException e, HttpServletRequest req) {
        return createErrorResponse(HttpStatus.FORBIDDEN, e, req);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, RuntimeException e, HttpServletRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getMessage());
        logger.warn("{} occurred on {} {}. Message={}",
                status, req.getMethod(), req.getRequestURI(), e.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
