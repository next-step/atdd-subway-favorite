package nextstep.subway.domain.exception;

import nextstep.common.exception.GlobalException;

public class FavoriteNotFoundException extends GlobalException {

    private static final String MESSAGE = "즐겨찾기를 찾을 수 없습니다.";

    public FavoriteNotFoundException() {
        super(MESSAGE);
    }
}
