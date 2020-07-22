package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public AuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        return null;
    }
}
