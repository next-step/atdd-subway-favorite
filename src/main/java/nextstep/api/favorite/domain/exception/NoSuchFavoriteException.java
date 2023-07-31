package nextstep.api.favorite.domain.exception;

import nextstep.api.SubwayException;

public class NoSuchFavoriteException extends SubwayException {
    public NoSuchFavoriteException(final String message) {
        super(message);
    }

    public static NoSuchFavoriteException from(final Long id) {
        return new NoSuchFavoriteException("즐겨찾기를 찾을 수 없습니다 : id=" + id);
    }
}
