package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubLoginRequest;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
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
        UserDetails user = userDetailsService.findMemberByEmail(email);
        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenByGithubLogin(GithubLoginRequest request) {
        String accessToken = githubClient.requestGithubToken(request.getCode());
        GithubProfileResponse profileResponse = githubClient.requestGithubProfile(accessToken);

        UserDetails user = userDetailsService.findOrCreateMember(profileResponse.getEmail(), profileResponse.getAge());
        String token = jwtTokenProvider.createToken(user.getEmail());

        return new TokenResponse(token);
    }
}
