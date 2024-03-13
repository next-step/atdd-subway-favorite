package nextstep.auth.application;

import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.application.dto.UserInfoResponse;
import nextstep.auth.domain.User;
import nextstep.auth.domain.UserGetter;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserGetter userGetter;

    public TokenService(JwtTokenProvider jwtTokenProvider, UserGetter userGetter) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userGetter = userGetter;
    }

    public TokenResponse createToken(String email, String password) {
        User user = userGetter.getUser(email, password);
        String token = jwtTokenProvider.createToken(user.getEmail());
        return new TokenResponse(token);
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        String email = jwtTokenProvider.getPrincipal(accessToken);
        User user = userGetter.getUser(email);
        return new UserInfoResponse(user.getEmail());
    }
}
