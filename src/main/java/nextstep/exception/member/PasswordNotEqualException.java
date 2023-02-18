package nextstep.exception.member;

import nextstep.exception.ErrorMessage;
import nextstep.exception.SubwayException;

public class PasswordNotEqualException extends SubwayException {
    public PasswordNotEqualException() {
        super(ErrorMessage.MEMBER_PASSWORD_NOT_EQUAL);
    }
}
