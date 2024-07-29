package nextstep.favorite.common;

import lombok.Getter;

@Getter
public enum FavoriteErrorMessage {

    NO_FAVORITE_EXIST("해당하는 즐겨찾기가 없습니다.");

    private final String message;

    FavoriteErrorMessage(String message) {
        this.message = message;
    }
}
