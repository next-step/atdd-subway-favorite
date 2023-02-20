package nextstep.favorite.application.exception;

import lombok.Getter;
import nextstep.common.exception.ErrorCode;

@Getter
public class FavoriteException extends RuntimeException {

    private final ErrorCode errorCode;

    public FavoriteException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
