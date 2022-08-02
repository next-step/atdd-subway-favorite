package nextstep.subway.exception;

import nextstep.common.BusinessException;

public class NotFoundStationException extends BusinessException {

    public static final String MESSAGE = "해당 역을 찾을 수 없습니다";

    public NotFoundStationException() {
        super(MESSAGE);
    }
}
