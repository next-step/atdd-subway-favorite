package nextstep.subway.applicaion.exception;

import lombok.Getter;
import nextstep.common.exception.ErrorCode;

@Getter
public class SubwayException extends RuntimeException {

    private final ErrorCode errorCode;

    public SubwayException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
