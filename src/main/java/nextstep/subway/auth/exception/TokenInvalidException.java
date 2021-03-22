package nextstep.subway.auth.exception;

import nextstep.subway.common.exception.BusinessException;

public class TokenInvalidException extends BusinessException {

	private static final String MESSAGE = "유효하지 않은 토큰입니다.";

	public TokenInvalidException() {
		super(MESSAGE);
	}
}
