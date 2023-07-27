package nextstep.favorite.exception;

import nextstep.common.StationBusinessException;

public class StationFavoriteCreateFailException extends StationBusinessException {
    public StationFavoriteCreateFailException(String message) {
        super(message);
    }
}
