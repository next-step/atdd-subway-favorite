package nextstep.favorite.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class FavoriteNotFoundException extends EntityNotFoundException {
    public FavoriteNotFoundException() {
        super("존재하지 않는 즐겨찾기입니다.");
    }
}
