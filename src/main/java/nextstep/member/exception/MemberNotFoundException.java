package nextstep.member.exception;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;

public class MemberNotFoundException extends SubwayException {

    public MemberNotFoundException(String email) {
        super(SubwayExceptionType.MEMBER_NOT_FOUND,
            String.format(SubwayExceptionType.MEMBER_NOT_FOUND.getMessage(), email));
    }
}
