package nextstep.exception;

public class MemberInvalidException extends IllegalArgumentException {

    private static final String INVALID_MEMBER = "유효한 멤버가 아닙니다.";

    public MemberInvalidException() {
        super(INVALID_MEMBER);
    }
}
