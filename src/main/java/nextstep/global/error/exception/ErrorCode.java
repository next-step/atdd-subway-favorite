package nextstep.global.error.exception;

public enum ErrorCode {
    NOT_FOUND_MEMBER("not found member", "400");

    private final String message;
    private final String status;

    ErrorCode(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
