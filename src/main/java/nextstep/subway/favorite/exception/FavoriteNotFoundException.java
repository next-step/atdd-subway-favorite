package nextstep.subway.favorite.exception;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;

public class FavoriteNotFoundException extends SubwayException {

    public FavoriteNotFoundException(Long favoriteId) {
        super(SubwayExceptionType.FAVORITE_NOT_FOUND,
            String.format(SubwayExceptionType.FAVORITE_NOT_FOUND.getMessage(), favoriteId));
    }
}
