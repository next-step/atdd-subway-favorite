package nextstep.subway.auth.exception;

public class NotFoundAuthenticationException extends RuntimeException {
    public NotFoundAuthenticationException() {
        super("유효하지 않은 인증 정보입니다. ");
    }
}
