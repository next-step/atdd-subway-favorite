package nextstep.path.ui.exception;

public class SameSourceAndTargetException extends IllegalArgumentException {

    private static final String MESSAGE = "출발역과 도착역은 달라야합니다.";

    public SameSourceAndTargetException() {
        super(MESSAGE);
    }
}

