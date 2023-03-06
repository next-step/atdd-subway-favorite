package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.GithubTokenRequest;
import nextstep.github.GithubClient;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final GithubClient githubClient;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService, GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.githubClient = githubClient;
    }

    public TokenResponse getToken(String email, String password) {
        Member member = memberService.findByEmailAndPassword(email, password);
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public GithubAccessTokenResponse getAccessToken(GithubTokenRequest tokenRequest) {
        return new GithubAccessTokenResponse(githubClient.getAccessTokenFromGithub(tokenRequest));
    }

    public GithubProfileResponse getUsersProfile(String authorization) {
        return githubClient.getUsersProfile(authorization);
    }
}
