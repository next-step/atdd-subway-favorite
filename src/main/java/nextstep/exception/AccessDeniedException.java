package nextstep.exception;

public class AccessDeniedException extends RuntimeException {
    private static final String ERROR_MESSAGE = "접근 권한이 없습니다.";

    public AccessDeniedException() {
        super(ERROR_MESSAGE);
    }
}
