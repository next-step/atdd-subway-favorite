package nextstep.path.application.exception;

public class NotAddedEndToPathsException extends NotAddedStationsToPathsException {
    private static final String MESSAGE = "도착역(%s)";

    public NotAddedEndToPathsException(String name) {
        super(String.format(MESSAGE, name));
    }
}
