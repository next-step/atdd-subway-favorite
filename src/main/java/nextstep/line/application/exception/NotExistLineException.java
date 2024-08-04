package nextstep.line.application.exception;

public class NotExistLineException extends IllegalStateException {

    private static final String MESSAGE = "존재하지 않는 노선입니다.";

    public NotExistLineException() {
        super(MESSAGE);
    }
}
