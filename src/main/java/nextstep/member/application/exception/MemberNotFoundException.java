package nextstep.member.application.exception;

public class MemberNotFoundException extends RuntimeException {
    private static final String MESSAGE = "등록되지 않은 회원입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
