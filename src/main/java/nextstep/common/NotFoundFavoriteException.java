package nextstep.common;

public class NotFoundFavoriteException extends NotFoundException {
    public NotFoundFavoriteException(Long favoriteId) {
        super(String.format("not found favorite : %d", favoriteId));

    }
}
