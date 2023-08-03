package nextstep.exception;

import static nextstep.exception.SubwayError.SECTION_DELETE_EXCEPTION;

public class SectionDeleteException extends SubwayException {
    public SectionDeleteException() {
        super(SECTION_DELETE_EXCEPTION);
    }
}
