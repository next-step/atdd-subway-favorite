package nextstep.exception;

public class UnauthorizedDeletionException extends RuntimeException {
    public UnauthorizedDeletionException() {
        super("삭제할 권한이 없습니다.");
    }
}
