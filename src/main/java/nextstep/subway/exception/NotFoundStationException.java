package nextstep.subway.exception;

public class NotFoundStationException extends RuntimeException{
    public NotFoundStationException(){
        super("not found station");
    }
}
