package nextstep.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedDeletionException extends SubwayException {
    public UnauthorizedDeletionException(Long favoriteId) {
        super("해당 즐겨찾기를 삭제할 권한이 없습니다. ID: " + favoriteId, HttpStatus.FORBIDDEN);
    }
}
