package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenDto;
import nextstep.auth.application.oauth.GithubClient;
import nextstep.auth.application.dto.UserDetailDto;
import nextstep.auth.ui.dto.TokenFromGithubRequestBody;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(
            UserDetailService userDetailService,
            JwtTokenProvider jwtTokenProvider,
            GithubClient githubClient
    ) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenDto createToken(String email, String password) {
        UserDetailDto userDetail = userDetailService.findByEmail(email);
        if (!userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetail.getEmail());

        return new TokenDto(token);
    }

    @Transactional
    public TokenDto createTokenFromGithub(String code) {
        String accessToken = this.githubClient.requestToken(new TokenFromGithubRequestBody(code));
        GithubProfileResponse githubProfileResponse = this.githubClient.requestProfile(accessToken);

        String email = githubProfileResponse.getEmail();

        UserDetailDto userDetail = userDetailService.findByEmailOrCreate(email);

        String token = jwtTokenProvider.createToken(userDetail.getEmail());
        return new TokenDto(token);
    }
}
