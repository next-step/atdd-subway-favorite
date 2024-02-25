package nextstep.core.member.exception;

import nextstep.common.exception.BadRequestException;

public class NotFoundMemberException extends BadRequestException {

    public NotFoundMemberException(String message) {
        super(message);
    }
}
