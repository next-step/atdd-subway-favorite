package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class CannotDeleteSectionException extends RuntimeException {

    public CannotDeleteSectionException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
