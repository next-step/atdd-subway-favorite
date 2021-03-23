package nextstep.subway.auth.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotValidPassword extends BusinessException {

	private static final String MESSAGE = "비밀번호 오류";

	public NotValidPassword() {
		super(MESSAGE);
	}
}
