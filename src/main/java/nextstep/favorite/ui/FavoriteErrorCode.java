package nextstep.favorite.ui;

import nextstep.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public enum FavoriteErrorCode implements ErrorCode {
    CANT_ADD_FAVORITE(HttpStatus.BAD_REQUEST),
    NOT_MY_FAVORITE(HttpStatus.FORBIDDEN);

    private final HttpStatus status;

    FavoriteErrorCode(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String code() {
        return name();
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
