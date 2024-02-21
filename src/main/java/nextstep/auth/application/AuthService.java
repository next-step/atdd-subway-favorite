package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TokenService tokenService;
    private GithubClient githubClient;

    private final UserDetailService userDetailService;

    public AuthService(TokenService tokenService, GithubClient githubClient, UserDetailService userDetailService) {
        this.tokenService = tokenService;
        this.githubClient = githubClient;
        this.userDetailService = userDetailService;
    }

    public TokenResponse login(String email, String password) {
        return new TokenResponse(tokenService.createToken(email, password).getAccessToken());
    }

    public TokenResponse loginGithub(String code) {
        final String accessToken = githubClient.requestGithubToken(code);
        final GithubProfileResponse profile = githubClient.requestGithubProfile(accessToken);

        userDetailService.findOrCreate(profile.getEmail());

        return new TokenResponse(tokenService.createToken(profile.getEmail(), code).getAccessToken());
    }
}
