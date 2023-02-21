package nextstep.exception;

public class NotAllowedAuthorizationException extends AuthorizationException {

    private static final String MESSAGE = "유효하지 않은 인증 정보입니다: %s";

    public NotAllowedAuthorizationException(String authentication) {
        super(String.format(MESSAGE, authentication));
    }
}
