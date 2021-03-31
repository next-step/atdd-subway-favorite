package nextstep.subway.auth.exception;

public class UnauthorizedException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "접근 권한이 없습니다.";

    public UnauthorizedException() {
        super(EXCEPTION_MESSAGE);
    }
}
