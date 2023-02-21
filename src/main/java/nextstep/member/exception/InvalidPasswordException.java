package nextstep.member.exception;

public class InvalidPasswordException extends RuntimeException {
    private static final String ERROR_MESSAGE = "멤버의 패스워드와 일치하지 않습니다.";

    public InvalidPasswordException() {
        super(ERROR_MESSAGE);
    }
}
