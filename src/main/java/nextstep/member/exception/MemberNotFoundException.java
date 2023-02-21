package nextstep.member.exception;

public class MemberNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "입력에 해당하는 멤버를 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
