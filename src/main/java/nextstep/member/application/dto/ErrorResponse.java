package nextstep.member.application.dto;

public class ErrorResponse {

    private String errorCode;

    private String errorMessage;

    private Object data;

    public ErrorResponse(String errorCode, String errorMessage, Object data) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
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
