package nextstep.auth.exception;

public class InvalidPasswordException extends RuntimeException{
    public static final String MESSAGE = "잘못된 비밀번호 입니다.";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
