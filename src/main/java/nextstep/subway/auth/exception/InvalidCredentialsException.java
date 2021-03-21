package nextstep.subway.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    public static String MESSAGE = "인증 정보가 잘못 되었습니다.";

    public InvalidCredentialsException() {
        super(MESSAGE);
    }
}
