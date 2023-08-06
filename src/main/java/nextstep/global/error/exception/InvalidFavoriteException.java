package nextstep.global.error.exception;

import nextstep.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidFavoriteException extends BusinessException {

    public InvalidFavoriteException(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST.value(), errorCode.getMessage());
    }

}
