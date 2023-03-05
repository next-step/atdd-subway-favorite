package nextstep.login.application.exception;

public class FailAuthorizationException extends RuntimeException {
    private static final String MESSAGE = "사용자 인증에 실패하였습니다.";

    public FailAuthorizationException() {
        super(MESSAGE);
    }
}
