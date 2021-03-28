package nextstep.subway.favorite.exception;

import nextstep.subway.common.exception.BusinessException;

public class AccessDeniedException extends BusinessException {

	private static final String MESSAGE = "접근 권한이 없습니다.";

	public AccessDeniedException() {
		super(MESSAGE);
	}
}
