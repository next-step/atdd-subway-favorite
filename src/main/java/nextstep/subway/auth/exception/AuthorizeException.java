package nextstep.subway.auth.exception;

public class AuthorizeException extends RuntimeException{
    public static final String MESSAGE = "해당 권한이 없습니다.";

    public AuthorizeException() {
        super(MESSAGE);
    }
}
