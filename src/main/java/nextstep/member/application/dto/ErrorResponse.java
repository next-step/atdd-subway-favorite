package nextstep.member.application.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public class ErrorResponse {

    private HttpStatus status;

    private String errorCode;

    private String errorMessage;

    private Object data;

    public ErrorResponse(HttpStatus status, String errorCode, String errorMessage, Object data) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getData() {
        return data;
    }
}
