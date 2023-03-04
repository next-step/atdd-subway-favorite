package nextstep.common.exception;

public class CanNotDeleteFavoriteException extends RuntimeException {
    private final ErrorCode errorCode;

    public CanNotDeleteFavoriteException() {
        super(ErrorCode.CAN_NOT_DELETE_FAVORITE.getMessage());
        this.errorCode = ErrorCode.CAN_NOT_DELETE_FAVORITE;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
