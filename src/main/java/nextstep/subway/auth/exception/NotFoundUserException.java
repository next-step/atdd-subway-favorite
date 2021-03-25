package nextstep.subway.auth.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotFoundUserException extends BusinessException {

	private static final String MESSAGE = "유저 정보를 찾을 수 없습니다.";

	public NotFoundUserException() {
		super(MESSAGE);
	}
}
