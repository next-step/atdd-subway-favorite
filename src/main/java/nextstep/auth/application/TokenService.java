package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.UserDetail;
import nextstep.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;
    private UserDetailService userDetailService;

    public TokenService(JwtTokenProvider jwtTokenProvider, GithubClient githubClient, UserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
        this.userDetailService = userDetailService;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetail userDetail = userDetailService.findUser(email);
        if (!userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetail.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessToken = githubClient.requestGithubToken(code);

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(accessToken);

        UserDetail userDetail = userDetailService.findOrCreateUser(githubProfileResponse.getEmail(), githubProfileResponse.getAge());

        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.createToken(userDetail.getEmail()))
                .build();

    }
}
