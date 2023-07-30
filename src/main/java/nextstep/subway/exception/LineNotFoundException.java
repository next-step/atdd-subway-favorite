package nextstep.subway.exception;

public class LineNotFoundException extends IllegalArgumentException{

    public LineNotFoundException(String msg) {
        super(msg);
    }
}
