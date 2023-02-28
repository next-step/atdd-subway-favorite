package nextstep.member.application.exception;

public class StationNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "지하철을 찾을 수 없습니다.";

    public StationNotFoundException() {
        super(MESSAGE);
    }
}
