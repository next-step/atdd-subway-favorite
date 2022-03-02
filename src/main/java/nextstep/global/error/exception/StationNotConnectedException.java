package nextstep.global.error.exception;

public class StationNotConnectedException extends RuntimeException {

    private static final String MESSAGE = "%s역과 %s역은 연결되어 있지 않습니다.";

    public StationNotConnectedException(String target, String source) {
        super(String.format(MESSAGE, target, source));
    }
}
