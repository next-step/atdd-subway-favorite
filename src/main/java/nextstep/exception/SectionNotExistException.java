package nextstep.exception;

import static nextstep.exception.SubwayError.SECTION_NOT_EXIST_EXCEPTION;

public class SectionNotExistException extends SubwayException {
    public SectionNotExistException() {
        super(SECTION_NOT_EXIST_EXCEPTION);
    }
}
