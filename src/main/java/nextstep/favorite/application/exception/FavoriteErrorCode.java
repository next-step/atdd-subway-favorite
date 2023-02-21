package nextstep.favorite.application.exception;

import nextstep.common.exception.ErrorCode;

public enum FavoriteErrorCode implements ErrorCode {
    NOT_FOUND_FAVORITE("요청한 즐겨찾기를 찾을 수 없습니다."),
    INVALID_REMOVE_REQUEST("잘못된 즐겨찾기 삭제 요청입니다."),
    INVALID_CREATED_EQUAL_STATION("즐겨찾기 생성시 동일한 역으로는 생성이 불가능합니다."),
    INVALID_CREATE_REQUEST_STATION("즐겨찾기 생성시 출발역 또는 도착역을 지정해야 합니다.");

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
