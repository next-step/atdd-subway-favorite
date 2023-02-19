package nextstep.member.domain.exception;

public class BadCredentialException extends RuntimeException {

    public static final String MESSAGE = "유저를 찾을 수 업습니다.";

    public BadCredentialException() {
        super(MESSAGE);
    }
}
