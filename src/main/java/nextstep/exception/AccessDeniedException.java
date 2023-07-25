package nextstep.exception;

public class AccessDeniedException extends RuntimeException {
    public static final String ERROR_MESSAGE = "접근 권한이 없습니다.";

    public AccessDeniedException() {
        super(ERROR_MESSAGE);
    }
}
