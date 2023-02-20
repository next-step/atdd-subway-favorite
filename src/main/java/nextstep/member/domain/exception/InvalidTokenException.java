package nextstep.member.domain.exception;

public class InvalidTokenException extends RuntimeException {
    public static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
