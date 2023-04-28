package nextstep.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AuthException{

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
