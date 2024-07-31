package nextstep.subway.domain.exception;

public class NotFoundFavoriteException extends SubwayDomainException {
    public NotFoundFavoriteException(Long id) {
        super(SubwayDomainExceptionType.NOT_FOUND_FAVORITE, "favoriteId " + id + " is not found");
    }
}
