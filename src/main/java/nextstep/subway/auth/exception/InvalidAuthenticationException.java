package nextstep.subway.auth.exception;

public class InvalidAuthenticationException extends RuntimeException {

    public static String MESSAGE = "유효한 인증 정보가 아닙니다.";

    public InvalidAuthenticationException() {
        super(MESSAGE);
    }
}
