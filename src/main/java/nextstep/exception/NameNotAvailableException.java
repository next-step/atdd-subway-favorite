package nextstep.exception;

import static nextstep.exception.SubwayError.NAME_NOT_AVAILABLE;

public class NameNotAvailableException extends SubwayException {
    public NameNotAvailableException() {
        super(NAME_NOT_AVAILABLE);
    }
}
