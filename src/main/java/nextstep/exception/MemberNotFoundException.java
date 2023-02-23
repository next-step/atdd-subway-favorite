package nextstep.exception;

public class MemberNotFoundException extends IllegalArgumentException {

    private static final String UNREGISTERED_MEMBER = "등록된 멤버가 아닙니다.";

    public MemberNotFoundException() {
        super(UNREGISTERED_MEMBER);
    }
}
