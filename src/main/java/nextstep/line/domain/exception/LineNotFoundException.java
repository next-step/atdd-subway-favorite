package nextstep.line.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class LineNotFoundException extends EntityNotFoundException {
    public LineNotFoundException() {
        super("존재하지 않는 노선입니다.");
    }
}
