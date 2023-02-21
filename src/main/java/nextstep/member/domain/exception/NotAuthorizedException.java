package nextstep.member.domain.exception;

public class NotAuthorizedException extends MemberException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
