package nextstep.member.application;

public class MemberNotFoundException extends RuntimeException {

    private final static String MESSAGE = "회원을 찾을 수 없습니다. (요청값: %s)";

    public MemberNotFoundException(String email) {
        super(String.format(MESSAGE, email));
    }
}
