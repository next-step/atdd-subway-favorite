package nextstep.exception;

public class InvalidAccessTokenException extends IllegalArgumentException {
    private static final String INVALID_TOKEN = "유효한 토큰이 아닙니다.";

    public InvalidAccessTokenException() {
        super(INVALID_TOKEN);
    }
}
