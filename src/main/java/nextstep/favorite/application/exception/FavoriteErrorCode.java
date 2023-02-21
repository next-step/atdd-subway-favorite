package nextstep.favorite.application.exception;

import nextstep.common.exception.ErrorCode;

public enum FavoriteErrorCode implements ErrorCode {
    NOT_FOUND_FAVORITE("요청한 즐겨찾기를 찾을 수 없습니다."),
    INVALID_REMOVE_REQUEST("잘못된 즐겨찾기 삭제 요청입니다.");

    private final String message;

    FavoriteErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String getCode() {
        return null;
    }
}
