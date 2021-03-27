package nextstep.subway.common;

import nextstep.subway.auth.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public void handle(HttpServletResponse response, UnauthorizedException e) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
