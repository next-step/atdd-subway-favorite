package nextstep.member.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class FavoriteNotFoundException extends EntityNotFoundException {
    public static final String ERROR_MESSAGE = "존재하지 않는 즐겨찾기입니다. ID=%d";

    public FavoriteNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
