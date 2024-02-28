package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.infra.GithubClient;
import nextstep.auth.presentation.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final UserDetailService userDetailService;
    private final GithubClient githubClient;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(
            UserDetailService userDetailService,
            GithubClient githubClient,
            JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.githubClient = githubClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetail userDetail = userDetailService.loadUser(email);
        if (!userDetail.isEquals(password)) {
            throw new AuthenticationException();
        }
        return new TokenResponse(jwtTokenProvider.createToken(userDetail.getEmail()));
    }


    public TokenResponse createToken(String code) {
        String token = githubClient.getAccessTokenFromGithub(code);
        UserDetail userDetail = userDetailService.loadUser(githubClient.getUserProfile(token).getEmail());
        return new TokenResponse(jwtTokenProvider.createToken(userDetail.getEmail()));
    }
}
