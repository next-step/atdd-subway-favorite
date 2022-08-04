package nextstep.subway.exception;

public class DuplicatedStationsException extends IllegalArgumentException {
    public DuplicatedStationsException() {
        super("duplicated stations in path");
    }
}
