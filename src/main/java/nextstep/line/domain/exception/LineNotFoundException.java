package nextstep.line.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class LineNotFoundException extends EntityNotFoundException {
    public static final String ERROR_MESSAGE = "존재하지 않는 노선입니다. ID=%d";

    public LineNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
