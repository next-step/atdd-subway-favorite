package nextstep.path.domain.exception;

public class PathNotFoundException extends PathException {
    private static String DEFAULT_MESSAGE = "경로를 찾을 수 없습니다.";

    public PathNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public PathNotFoundException(String message) {
        super(message + DEFAULT_MESSAGE);
    }
}
