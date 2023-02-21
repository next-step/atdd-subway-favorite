package nextstep.exception.member;

import nextstep.exception.ErrorMessage;
import nextstep.exception.SubwayException;

public class MemberNotFoundException extends SubwayException {
    public MemberNotFoundException() {
        super(ErrorMessage.MEMBER_NOT_FOUND);
    }


}
