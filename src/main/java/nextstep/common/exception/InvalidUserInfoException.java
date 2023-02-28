package nextstep.common.exception;

public class InvalidUserInfoException extends RuntimeException {

    public InvalidUserInfoException() {
        super("Id or password is wrong.");
    }
}
