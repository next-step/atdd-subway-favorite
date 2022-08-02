package nextstep.member.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException() {
        super("not found member");
    }
}
