package nextstep.exception;

import static nextstep.exception.SubwayError.NOT_FOUND_SECTION;

public class SectionNotFoundException extends SubwayException {
    public SectionNotFoundException() {
        super(NOT_FOUND_SECTION);
    }
}
