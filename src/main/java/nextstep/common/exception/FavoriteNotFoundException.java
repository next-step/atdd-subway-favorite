package nextstep.common.exception;

import org.springframework.http.HttpStatus;

public class FavoriteNotFoundException extends SubwayException {
    public FavoriteNotFoundException(Long favoriteId) {
        super("즐겨찾기를 찾을 수 없습니다. ID: " + favoriteId, HttpStatus.BAD_REQUEST);
    }
}
