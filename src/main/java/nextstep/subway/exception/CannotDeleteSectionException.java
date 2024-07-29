package nextstep.subway.exception;

import nextstep.subway.common.ErrorMessage;

public class CannotDeleteSectionException extends RuntimeException {

    public CannotDeleteSectionException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
