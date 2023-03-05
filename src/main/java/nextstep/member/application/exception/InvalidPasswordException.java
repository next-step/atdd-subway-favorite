package nextstep.member.application.exception;

public class InvalidPasswordException extends RuntimeException {
    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
