package nextstep.exception;

import org.springframework.http.HttpStatus;

public class InvalidCodeException extends AuthException{

    private static final String MESSAGE = "유효하지 않은 코드입니다.";

    public InvalidCodeException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
