package nextstep.line.application.exception;

public class NotLessThanExistingDistanceException extends IllegalArgumentException {
    private static final String MESSAGE = "새로운 구간의 길이는 기존 구간의 길이보다 작아야합니다.";

    public NotLessThanExistingDistanceException() {
        super(MESSAGE);
    }
}
