package nextstep.Exception;

public class NoAuthorizationHeaderException extends RuntimeException {
    private static final String message = "Authorization 헤더 정보가 포함돼있지 않습니다.";

    public NoAuthorizationHeaderException() {
        super(message);
    }
}
