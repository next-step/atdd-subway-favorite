package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class NoLineExistException extends RuntimeException {

    public NoLineExistException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
