package nextstep.subway.auth.exception;

public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException() {
        super("인증이 올바르지 않습니다.");
    }
}
