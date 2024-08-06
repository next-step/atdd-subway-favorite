package nextstep.path.application.exception;

public class NotAddedStationsToPathsException extends IllegalStateException {
    private static final String MESSAGE = "구간에 추가되지 않은 역입니다. 추가되지 않은 역: %s";

    public NotAddedStationsToPathsException(String station) {
        super(String.format(MESSAGE, station));
    }
}
