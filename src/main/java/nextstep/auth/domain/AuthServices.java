package nextstep.auth.domain;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import nextstep.util.AuthUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthServices {

    private final List<AuthService> values;

    public AuthService findService(String header) {
        return values.stream()
                .filter(s -> AuthUtil.match(header, s.getPrefix()))
                .findAny()
                .orElseThrow(AuthenticationException::new);
    }
}
