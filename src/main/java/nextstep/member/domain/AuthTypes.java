package nextstep.member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthTypes {

    private final List<AuthType> values;

    public static AuthTypes from(List<AuthType> authTypes) {
        return new AuthTypes(authTypes);
    }

    public AuthType findAuth(String header) {
        return values.stream()
                .filter(a -> a.match(header))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
