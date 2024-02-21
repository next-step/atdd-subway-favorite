package nextstep.auth.application;

import nextstep.global.AuthenticationException;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private UserDetailService userDetailService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        final UserDetails userDetails = userDetailService.findByEmail(email);
        if (!userDetails.isSamePassword(password)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(userDetails.getEmail());

        return new TokenResponse(token);
    }
}
