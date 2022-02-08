package nextstep.subway.common.handler;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String exceptionMessage;

<<<<<<< HEAD
    private ExceptionResponse(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public static ExceptionResponse of(String exceptionMessage) {
        return new ExceptionResponse(exceptionMessage);
    }
=======
    public ExceptionResponse(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
>>>>>>> aaeadc3 (init: 뼈대코드 변경)
}
