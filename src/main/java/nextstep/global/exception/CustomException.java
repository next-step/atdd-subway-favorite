package nextstep.global.exception;

public class CustomException extends RuntimeException {

    private final int code;
    private final String message;

    public CustomException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
