package nextstep.member.application;

public class NotExistsMemberException extends RuntimeException {
    public NotExistsMemberException(String message) {
        super(message);
    }
}
