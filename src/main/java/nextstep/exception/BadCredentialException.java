package nextstep.exception;

public class BadCredentialException extends RuntimeException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public BadCredentialException() {
        super(MESSAGE);
    }
}
