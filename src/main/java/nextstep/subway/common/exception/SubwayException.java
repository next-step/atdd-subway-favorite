package nextstep.subway.common.exception;

import lombok.Getter;

@Getter
public class SubwayException extends RuntimeException {

    private final SubwayExceptionType subwayExceptionType;
    private final String message;

    public SubwayException(SubwayExceptionType subwayExceptionType) {
        super();
        this.subwayExceptionType = subwayExceptionType;
        this.message = subwayExceptionType.getMessage();
    }

    public SubwayException(SubwayExceptionType subwayExceptionType, String message) {
        super();
        this.subwayExceptionType = subwayExceptionType;
        this.message = message;
    }
}
