package nextstep.exception;

public class InvalidAccessTokenException extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다. (토큰: %s)";

    public InvalidAccessTokenException(String token) {
        super(String.format(MESSAGE, token));
    }
}
