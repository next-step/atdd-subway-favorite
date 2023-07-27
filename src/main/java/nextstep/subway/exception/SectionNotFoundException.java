package nextstep.subway.exception;

public class SectionNotFoundException extends IllegalArgumentException{

    public SectionNotFoundException(String msg) {
        super(msg);
    }
}
