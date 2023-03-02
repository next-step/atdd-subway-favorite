package nextstep.exception;

import lombok.NoArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;

@NoArgsConstructor
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, HttpClientErrorException e) {
        super(message, e);
    }
}
