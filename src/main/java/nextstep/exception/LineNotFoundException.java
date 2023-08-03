package nextstep.exception;

import static nextstep.exception.SubwayError.NOT_FOUND_LINE;

public class LineNotFoundException extends SubwayException {
    public LineNotFoundException() {
        super(NOT_FOUND_LINE);
    }
}
