package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import subway.constant.SubwayMessage;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends SubwayException {

    public AuthenticationException(SubwayMessage subwayMessage) {
        super(subwayMessage);
    }

    public AuthenticationException(long code, String message) {
        super(code, message);
    }
}
