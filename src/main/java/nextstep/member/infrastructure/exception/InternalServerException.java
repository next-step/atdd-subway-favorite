package nextstep.member.infrastructure.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException() {
        super("Internal Server Error");
    }
}
