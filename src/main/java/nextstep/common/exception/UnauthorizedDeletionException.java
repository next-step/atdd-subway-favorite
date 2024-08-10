package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class UnauthorizedDeletionException extends BusinessException{
    public UnauthorizedDeletionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnauthorizedDeletionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
