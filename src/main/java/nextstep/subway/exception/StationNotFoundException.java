package nextstep.subway.exception;

public class StationNotFoundException extends IllegalArgumentException{

    public StationNotFoundException(String msg) {
        super(msg);
    }
}
