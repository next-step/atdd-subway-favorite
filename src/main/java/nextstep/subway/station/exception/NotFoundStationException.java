package nextstep.subway.station.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotFoundStationException extends BusinessException {

	private static final String MESSAGE = "존재하지 않는 역입니다.";

	public NotFoundStationException() {
		super(MESSAGE);
	}
}
