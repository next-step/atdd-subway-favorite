package nextstep.member.application.exception;

public class MemberNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 회원을 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
