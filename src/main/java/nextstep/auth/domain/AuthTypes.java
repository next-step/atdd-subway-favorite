package nextstep.auth.domain;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthTypes {

    private final List<AuthType> values;

    public AuthType findAuth(String header) {
        return values.stream()
                .filter(a -> a.match(header))
                .findAny()
                .orElseThrow(AuthenticationException::new);
    }
}
