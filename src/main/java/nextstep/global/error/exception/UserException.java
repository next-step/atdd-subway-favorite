package nextstep.global.error.exception;

public class UserException extends RuntimeException {
    private int status;

    public UserException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
