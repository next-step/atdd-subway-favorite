package nextstep.path.application.exception;

public class NotAddedStartToPathsException extends NotAddedStationsToPathsException {
    private static final String MESSAGE = "출발역(%s)";

    public NotAddedStartToPathsException(String name) {
        super(String.format(MESSAGE, name));
    }
}
