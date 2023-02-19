package nextstep.member.domain.exception;

public class PasswordMismatchException extends IllegalArgumentException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordMismatchException() {
        super(MESSAGE);
    }
}
