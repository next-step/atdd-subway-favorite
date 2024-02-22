package nextstep.favorite.application.exception;

import nextstep.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FavoriteException extends ApplicationException {

    private final static String NOT_CREATED = "즐겨찾기 추가에 실패했습니다.";

    public FavoriteException(HttpStatus status, String message) {
        super(status, message);
    }

    public static class NotFoundException extends FavoriteException {

        public NotFoundException(HttpStatus status, String message) {
            super(status, message);
        }
    }

    public static class NotCreatedException extends FavoriteException {

        public NotCreatedException() {
            super(HttpStatus.BAD_REQUEST, NOT_CREATED);
        }
    }
}
