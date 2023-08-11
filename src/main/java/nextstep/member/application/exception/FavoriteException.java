package nextstep.member.application.exception;

public class FavoriteException extends RuntimeException {
    public FavoriteException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
