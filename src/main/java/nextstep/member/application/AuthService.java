package nextstep.member.application;

import nextstep.member.application.client.GithubClient;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;
    private final GithubClient githubClient;

    public AuthService(MemberService memberService, JwtTokenProvider tokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse login(String email, String password) {
        MemberResponse member = memberService.findMember(email, password);
        String token = tokenProvider.createToken(member.getEmail(), RoleType.normalUser());
        return TokenResponse.of(token);
    }

    public GithubAccessTokenResponse loginByGithub(String code) {
        String githubToken = githubClient.getAccessTokenFromGithub(code);
        String accessToken = tokenProvider.createToken(githubToken, RoleType.normalUser());

        return new GithubAccessTokenResponse(accessToken);
    }

    public GithubProfileResponse getUserInfoFromGithub(String accessToken) {
        validateToken(accessToken);

        String githubToken = tokenProvider.getPrincipal(accessToken);
        return githubClient.getGithubProfileFromGithub(githubToken);
    }

    private void validateToken(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new RuntimeException();
        }
    }

}
