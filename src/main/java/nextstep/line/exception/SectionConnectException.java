package nextstep.line.exception;

import nextstep.common.exception.BadRequestException;

public class SectionConnectException extends BadRequestException {
    public SectionConnectException(final String message) {
        super(message);
    }
}
