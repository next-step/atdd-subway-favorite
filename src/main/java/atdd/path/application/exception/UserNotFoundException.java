package atdd.path.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("자원을 찾을 수 없습니다.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
