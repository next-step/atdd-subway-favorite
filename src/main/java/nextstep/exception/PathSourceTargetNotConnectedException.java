package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PathSourceTargetNotConnectedException extends RuntimeException {
    public PathSourceTargetNotConnectedException(String message) {
        super(message);
    }

    public PathSourceTargetNotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
