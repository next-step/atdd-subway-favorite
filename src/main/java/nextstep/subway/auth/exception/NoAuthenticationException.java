package nextstep.subway.auth.exception;

public class NoAuthenticationException extends RuntimeException {
    private static final String ERROR_MESSAGE = "권한이 없습니다.";

    public NoAuthenticationException() {
        super(ERROR_MESSAGE);
    }
}
