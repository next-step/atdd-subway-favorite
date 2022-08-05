package nextstep.auth.exception;

public class OutputStreamException extends RuntimeException {

    public static final String MESSAGE = "OutputStream을 만들 수 없습니다";

    public OutputStreamException() {
        super(MESSAGE);
    }
}
