package nextstep.line.domain.exception;

public class LineNotFoundException extends LineException {
    private static String DEFAULT_MESSAGE = "노선을 찾을 수 없습니다.";
    public LineNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
