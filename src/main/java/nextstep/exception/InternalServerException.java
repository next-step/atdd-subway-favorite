package nextstep.exception;

import nextstep.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InternalServerException extends ApplicationException {
    public InternalServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
