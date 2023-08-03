package nextstep.exception;

import static nextstep.exception.SubwayError.SECTION_ADD_EXCEPTION;

public class SectionAddException extends SubwayException {
    public SectionAddException() {
        super(SECTION_ADD_EXCEPTION);
    }
}
