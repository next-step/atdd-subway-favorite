package nextstep.member.application;

import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.GithubClient;
import nextstep.member.infrastructure.GithubProfileResponse;
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

    public TokenResponse createToken(TokenRequest request) {
        Member member = memberService.findMemberByEmail(request.getEmail()).orElseThrow(RuntimeException::new);
        if (!member.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException();
        }
        String token = tokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse getGithubToken(GithubTokenRequest request) {
        String githubAccessToken = githubClient.getAccessTokenFromGithub(request.getCode());
        GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(githubAccessToken);
        Member member = memberService.findMemberByEmail(githubProfileResponse.getEmail())
            .orElse(memberService.createMember(githubProfileResponse));
        String token = tokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
