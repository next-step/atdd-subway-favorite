package nextstep.subway.exception;

import nextstep.common.StationBusinessException;

public class StationLineCreateException extends StationBusinessException {
	public StationLineCreateException(String message) {
		super(message);
	}
}
