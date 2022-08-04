package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundStationException extends RuntimeException{
    public NotFoundStationException(){
        super("not found station");
    }
}
