package nextstep.subway.member.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotExistEmailException extends BusinessException {

	private static final String MESSAGE = "존재하지 않는 이메일 입니다.";

	public NotExistEmailException() {
		super(MESSAGE);
	}
}
