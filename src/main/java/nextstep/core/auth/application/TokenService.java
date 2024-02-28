package nextstep.core.auth.application;

import nextstep.core.auth.application.dto.GithubProfileResponse;
import nextstep.core.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String value) {
        return new TokenResponse(jwtTokenProvider.createToken(value));
    }

    public GithubProfileResponse requestGithubProfile(String code) {
        return githubClient.requestGithubProfile(code);
    }
}
