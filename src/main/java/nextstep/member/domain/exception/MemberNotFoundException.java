package nextstep.member.domain.exception;

public class MemberNotFoundException extends MemberException {
    private static String DEFAULT_MESSAGE = "존재하지 않는 멤버입니다.";
    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
