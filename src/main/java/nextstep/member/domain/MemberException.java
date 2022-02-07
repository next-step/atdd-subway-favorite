package nextstep.member.domain;

public class MemberException extends RuntimeException {
    private MemberException(String message) {
        super(message);
    }

    public static class NotFound extends MemberException {
        public NotFound(Long id) {
            super("Not found member; id=" + id);
        }
    }
}
