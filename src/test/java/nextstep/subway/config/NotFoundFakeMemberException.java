package nextstep.subway.config;

import nextstep.member.application.exception.ErrorCode;
import nextstep.member.application.exception.MemberException;

public class NotFoundFakeMemberException extends MemberException {

    public NotFoundFakeMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
