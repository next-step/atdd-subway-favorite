package nextstep.subway.auth.exception;

public class UnauthorizedException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE_UNAUTHORIZED = "유효하지 않은 접근입니다. 로그인을 해주세요.";

    public UnauthorizedException() {
        super(EXCEPTION_MESSAGE_UNAUTHORIZED);
    }
}
