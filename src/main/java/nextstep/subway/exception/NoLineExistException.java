package nextstep.subway.exception;

import nextstep.subway.common.ErrorMessage;

public class NoLineExistException extends RuntimeException {

    public NoLineExistException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
