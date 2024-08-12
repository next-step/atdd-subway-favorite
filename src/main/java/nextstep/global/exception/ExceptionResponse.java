package nextstep.global.exception;

public class ExceptionResponse {

    private final int code;
    private final String message;

    public ExceptionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
