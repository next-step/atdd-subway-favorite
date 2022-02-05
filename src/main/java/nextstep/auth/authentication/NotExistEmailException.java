package nextstep.auth.authentication;

public class NotExistEmailException extends RuntimeException{
    public static final String MESSAGE = "존재하지 않는 이메일입니다.";

    public NotExistEmailException() {
        super(MESSAGE);
    }
}
