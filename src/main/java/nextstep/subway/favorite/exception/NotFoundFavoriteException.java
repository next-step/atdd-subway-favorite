package nextstep.subway.favorite.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotFoundFavoriteException extends BusinessException {

	private static final String MESSAGE = "해당하는 즐겨찾기가 없습니다.";

	public NotFoundFavoriteException() {
		super(MESSAGE);
	}
}
