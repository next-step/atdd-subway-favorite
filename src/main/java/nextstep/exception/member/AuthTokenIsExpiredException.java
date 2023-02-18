package nextstep.exception.member;

import nextstep.exception.ErrorMessage;
import nextstep.exception.SubwayException;

public class AuthTokenIsExpiredException extends SubwayException {
    public AuthTokenIsExpiredException() {
        super(ErrorMessage.TOKEN_IS_EXPIRED);
    }
}
