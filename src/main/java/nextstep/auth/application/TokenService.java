package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.GithubTokenRequest;
import nextstep.auth.application.dto.GithubTokenResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.UserDetail;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetail userDetail = userDetailsService.getUser(email);
        if (!userDetail.matchesPassword(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetail.getEmail());

        return new TokenResponse(token);
    }

    public GithubTokenResponse getAccessToken(GithubTokenRequest request) {
        String accessToken = githubClient.requestGithubToken(request.getCode());
        GithubProfileResponse profileResponse = githubClient.requestGithubProfile(accessToken);

        UserDetail userDetail = userDetailsService.getGithubUser(profileResponse.getEmail(), profileResponse.getAge());

        String token = jwtTokenProvider.createToken(userDetail.getEmail());

        return new GithubTokenResponse(token);
    }
}
