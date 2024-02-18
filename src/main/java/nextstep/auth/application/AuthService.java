package nextstep.auth.application;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.common.exception.UnauthorizedException;
import nextstep.member.application.JwtTokenProvider;

public class AuthService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final UserDetailsService userDetailsService, final JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(final String email, final String password) {
        final UserDetail userDetail = userDetailsService.loadUserByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("아이디와 비밀번호를 확인해주세요."));

        if (userDetail.isPasswordMismatch(password)) {
            throw new UnauthorizedException("아이디와 비밀번호를 확인해주세요.");
        }

        final String token = jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail());

        return new AuthResponse(token);
    }
}
