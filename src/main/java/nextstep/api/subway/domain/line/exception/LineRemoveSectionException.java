package nextstep.api.subway.domain.line.exception;

import nextstep.api.SubwayException;

public class LineRemoveSectionException extends SubwayException {
    private static final String PREFIX = "구간 삭제 실패) ";

    public LineRemoveSectionException(final String message) {
        super(PREFIX + message);
    }
}
