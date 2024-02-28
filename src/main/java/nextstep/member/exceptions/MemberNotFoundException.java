package nextstep.member.exceptions;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String email) {
        super(email);
    }
}
