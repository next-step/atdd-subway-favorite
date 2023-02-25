package nextstep.common.auth;

public class InvalidTokenException extends RuntimeException {
    private static final String MESSAGE = "유효한 토큰이 아닙니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
