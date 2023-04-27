package nextstep.exception;

public class InvalidCodeException extends AuthException{

    private static final String MESSAGE = "유효하지 않은 코드입니다.";

    public InvalidCodeException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
