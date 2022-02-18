package nextstep.auth.authorization;

public class NoAuthorizedException extends RuntimeException {
    public NoAuthorizedException() {
        super("인증 에러입니다.");
    }
}