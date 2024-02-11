package nextstep.line.exception;

import nextstep.common.exception.BadRequestException;

public class SectionDisconnectException extends BadRequestException {
    public SectionDisconnectException(final String message) {
        super(message);
    }
}
