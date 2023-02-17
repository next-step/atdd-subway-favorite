package nextstep.exception;

public class AuthorizationException extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 인증 정보입니다: %s";

    public AuthorizationException(String authentication) {
        super(String.format(MESSAGE, authentication));
    }
}
