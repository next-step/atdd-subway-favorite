package nextstep.subway.favorite.exception;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;

public class UnauthorizedFavoriteAccessException extends SubwayException {
    public UnauthorizedFavoriteAccessException(Long memberId, Long favoriteId) {
        super(SubwayExceptionType.UNAUTHORIZED_FAVORITE_ACCESS,
            String.format(SubwayExceptionType.UNAUTHORIZED_FAVORITE_ACCESS.getMessage(),
                memberId, favoriteId));
    }
}
