package nextstep.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidSectionException extends SubwayException {
    public InvalidSectionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
