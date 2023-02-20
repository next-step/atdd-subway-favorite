package nextstep.member.domain.exception;

public class MemberNotFoundException extends RuntimeException {
    private static final String MESSAGE = "유저를 찾을 수 업습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
