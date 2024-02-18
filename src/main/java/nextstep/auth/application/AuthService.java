package nextstep.auth.application;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.member.application.JwtTokenProvider;

public class AuthService {
    public AuthService(final UserDetailsService userDetailsService, final JwtTokenProvider jwtTokenProvider) {
    }

    public AuthResponse login(final String email, final String password) {
        return new AuthResponse();
    }
}
