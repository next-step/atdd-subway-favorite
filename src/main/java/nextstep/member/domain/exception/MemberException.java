package nextstep.member.domain.exception;

public class MemberException extends RuntimeException {
    private static String DEFAULT_MESSAGE = "멤버 오류 : ";
    public MemberException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberException(Throwable cause) {
        super(cause);
    }

    protected MemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
