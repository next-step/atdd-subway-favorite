package nextstep.favorite.exception;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;

public class DuplicateFavoriteException extends SubwayException {
    public DuplicateFavoriteException(Long memberId, Long sourceStationId, Long targetStationId) {
        super(SubwayExceptionType.DUPLICATE_FAVORITE,
            String.format(SubwayExceptionType.DUPLICATE_FAVORITE.getMessage(),
                memberId, sourceStationId, targetStationId));
    }
}
