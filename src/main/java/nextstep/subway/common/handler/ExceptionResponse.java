package nextstep.subway.common.handler;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String exceptionMessage;

    private ExceptionResponse(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public static ExceptionResponse of(String exceptionMessage) {
        return new ExceptionResponse(exceptionMessage);
    }
}
