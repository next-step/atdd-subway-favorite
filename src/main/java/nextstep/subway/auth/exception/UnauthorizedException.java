package nextstep.subway.auth.exception;

import nextstep.subway.common.exception.BusinessException;

public class UnauthorizedException extends BusinessException {

	private static final String MESSAGE = "사용 권한이 없습니다.";

	public UnauthorizedException() {
		super(MESSAGE);
	}
}
