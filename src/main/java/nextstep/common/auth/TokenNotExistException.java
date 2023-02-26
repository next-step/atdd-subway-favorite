package nextstep.common.auth;

public class TokenNotExistException extends RuntimeException {
    private static final String MESSAGE = "인증 토큰이 존재하지 않습니다.";

    public TokenNotExistException() {
        super(MESSAGE);
    }
}
