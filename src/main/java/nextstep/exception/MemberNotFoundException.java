package nextstep.exception;

public class MemberNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 회원이 존재하지 않습니다. (e-mail: %s)";

    public MemberNotFoundException(String email) {
        super(String.format(MESSAGE, email));
    }
}
