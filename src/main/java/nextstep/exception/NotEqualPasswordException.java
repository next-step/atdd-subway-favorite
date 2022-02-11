package nextstep.exception;

public class NotEqualPasswordException extends RuntimeException {
    private static final String MESSAGE = "아이디와 패스워드가 일치하지 않습니다.";

    public NotEqualPasswordException() {
        super(MESSAGE);
    }
}
