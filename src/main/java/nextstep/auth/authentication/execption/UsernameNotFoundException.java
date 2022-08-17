package nextstep.auth.authentication.execption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UsernameNotFoundException extends AuthenticationException {
    public static final String USER_NOT_FOUND = "인증 유저를 찾을 수 없습니다.";

    public UsernameNotFoundException(String message) {
        super(message);
    }
}
