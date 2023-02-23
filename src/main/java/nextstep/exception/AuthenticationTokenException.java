package nextstep.exception;

public class AuthenticationTokenException extends IllegalArgumentException {
    private static final String INVALID_TOKEN = "유효한 토큰이 아닙니다.";

    public AuthenticationTokenException() {
        super(INVALID_TOKEN);
    }
}
