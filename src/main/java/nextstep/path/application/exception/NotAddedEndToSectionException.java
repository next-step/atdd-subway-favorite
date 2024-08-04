package nextstep.path.application.exception;

public class NotAddedEndToSectionException extends NotAddedStationsToSectionException {
    private static final String MESSAGE = "도착역(%s)";

    public NotAddedEndToSectionException(String name) {
        super(String.format(MESSAGE, name));
    }
}
