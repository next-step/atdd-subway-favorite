package nextstep.auth.exception;

public class EmptyAuthorizationHeaderException extends AuthorizationException {

    public static final String MESSAGE = "로그인을 진행해주세요.";

    public EmptyAuthorizationHeaderException() {
        super(MESSAGE);
    }
}
