package nextstep.member.domain.exception;

public class InvalidUserInfoException extends RuntimeException {

    public InvalidUserInfoException() {
        super("Id or password is wrong.");
    }
}
