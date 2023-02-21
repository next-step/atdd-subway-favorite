package nextstep.member.exception;

public class InvalidTokenException extends RuntimeException {
    public static final String ERROR_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(ERROR_MESSAGE);
    }
}
