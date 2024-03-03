package nextstep.subway.application.exception;

import nextstep.common.error.exception.BadRequestException;

public class CanNotFindPathException extends BadRequestException {

    public CanNotFindPathException(String msg) {
        super(msg);
    }
}
