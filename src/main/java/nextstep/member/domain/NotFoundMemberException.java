package nextstep.member.domain;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException() {
        super("not found member");
    }
}
