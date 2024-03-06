package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.OAuth2ProfileResponse;
import nextstep.auth.application.dto.OAuth2LoginRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailService.findMemberByEmail(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetails.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenByGithubLogin(OAuth2LoginRequest request) {
        String accessToken = githubClient.requestToken(request.getCode());
        if (!StringUtils.hasText(accessToken)) {
            throw new AuthenticationException();
        }

        OAuth2ProfileResponse profileResponse = githubClient.requestProfile(accessToken);
        UserDetails userDetails = userDetailService.findOrCreateMember(profileResponse);

        String token = jwtTokenProvider.createToken(userDetails.getEmail());

        return new TokenResponse(token);
    }
}
