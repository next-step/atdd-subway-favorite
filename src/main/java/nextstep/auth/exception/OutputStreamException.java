package nextstep.auth.exception;

public class OutputStreamException extends RuntimeException {
    public OutputStreamException() {
        super("OutputStream을 만들 수 없습니다");
    }
}
