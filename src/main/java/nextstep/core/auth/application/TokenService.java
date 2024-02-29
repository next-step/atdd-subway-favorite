package nextstep.core.auth.application;

import nextstep.core.auth.application.dto.GithubProfileResponse;
import nextstep.core.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        userDetailsService.verifyUser(email, password);
        return new TokenResponse(jwtTokenProvider.createToken(email));
    }

    public TokenResponse createTokenByGithub(String code) {
        GithubProfileResponse githubProfileResponse = requestGithubProfile(code);
        String userInfo = userDetailsService.findOrCreate(githubProfileResponse.getEmail());

        return new TokenResponse(jwtTokenProvider.createToken(userInfo));
    }

    public GithubProfileResponse requestGithubProfile(String code) {
        return githubClient.requestGithubProfile(code);
    }
}
