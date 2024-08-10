package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
