package nextstep.favorite.domain.exception;

import nextstep.common.exception.EntityNotFoundException;

public class FavoriteNotFoundException extends EntityNotFoundException {
    public static final String ERROR_MESSAGE = "존재하지 않는 즐겨찾기입니다.";

    public FavoriteNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
