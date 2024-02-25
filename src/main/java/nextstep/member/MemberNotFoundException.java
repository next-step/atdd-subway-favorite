package nextstep.member;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String email) {
        super(email);
    }
}
