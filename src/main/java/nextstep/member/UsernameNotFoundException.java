package nextstep.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String username) {
        super(String.format("UsernameNotFoundException with username: %s", username));
    }
}
