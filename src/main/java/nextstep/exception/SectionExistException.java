package nextstep.exception;

import static nextstep.exception.SubwayError.SECTION_EXIST_EXCEPTION;

public class SectionExistException extends SubwayException {
    public SectionExistException() {
        super(SECTION_EXIST_EXCEPTION);
    }
}
