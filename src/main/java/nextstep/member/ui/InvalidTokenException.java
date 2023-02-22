package nextstep.member.ui;

public class InvalidTokenException extends RuntimeException {
    private final static String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
