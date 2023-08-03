package nextstep.exception;

import static nextstep.exception.SubwayError.COLOR_NOT_AVAILABLE;

public class ColorNotAvailableException extends SubwayException {
    public ColorNotAvailableException() {
        super(COLOR_NOT_AVAILABLE);
    }
}
