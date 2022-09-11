package nextstep.common.exception;

import lombok.Getter;
import nextstep.common.exception.code.ResponseCode;

@Getter
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.toString());
        this.responseCode = responseCode;
    }
}
