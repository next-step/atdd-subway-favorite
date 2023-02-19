package nextstep.auth.exception;

public class EmptyAuthorizationHeaderException extends RuntimeException {

    public static final String MESSAGE = "유효하지 않은 요청입니다.";

    public EmptyAuthorizationHeaderException() {
        super(MESSAGE);
    }
}
