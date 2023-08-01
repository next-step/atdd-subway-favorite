package nextstep.auth.token;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.common.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN_EXCEPTION);
    }
}
