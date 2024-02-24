package nextstep.auth.application;

import java.util.Objects;
import nextstep.auth.AuthenticationException;
import nextstep.auth.domain.UserDetail;
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
        final UserDetail userDetail = userDetailService.findByEmail(email);
        if (userDetail.getPassword() != null && !userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        final String token = jwtTokenProvider.createToken(userDetail.getEmail());

        return new TokenResponse(token);
    }

}
