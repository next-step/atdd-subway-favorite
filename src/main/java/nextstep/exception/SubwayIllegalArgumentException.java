package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubwayIllegalArgumentException extends IllegalArgumentException {
    public SubwayIllegalArgumentException(String message){
        super(message);
    }
}
