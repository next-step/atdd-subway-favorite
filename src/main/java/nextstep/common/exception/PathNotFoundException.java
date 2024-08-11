package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class PathNotFoundException  extends BusinessException{
    public PathNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public PathNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}

