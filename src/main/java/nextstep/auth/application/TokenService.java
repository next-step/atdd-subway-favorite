package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.UserDetail;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(
            JwtTokenProvider jwtTokenProvider,
            GithubClient githubClient,
            UserDetailService userDetailService
    ) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetail userDetail = userDetailService.getUser(email);
        if (!userDetail.checkPassword(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetail.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessToken = githubClient.requestToken(code);
        GithubProfileResponse profile = githubClient.requestUser(accessToken);

        userDetailService.getGithubUser(profile.getEmail(), profile.getAge());

        return new TokenResponse(accessToken);
    }
}
