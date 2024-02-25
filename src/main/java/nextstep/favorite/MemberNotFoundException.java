package nextstep.favorite;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String email) {
        super(email);
    }
}
