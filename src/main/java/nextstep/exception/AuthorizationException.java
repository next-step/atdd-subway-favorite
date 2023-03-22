package nextstep.exception;

public class AuthorizationException extends RuntimeException {

    private String code;

    private Object data;

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public AuthorizationException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

