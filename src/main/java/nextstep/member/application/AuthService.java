package nextstep.member.application;

import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;
    private final GithubClient githubClient;

    AuthService(MemberService memberService, JwtTokenProvider tokenProvider,
        GithubClient githubClient) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberService.validateMemberAndReturn(request);
        String token = tokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse loginWithGithub(GithubLoginRequest request) {
        String githubAccessToken = githubClient.getAccessTokenFromGithub(request.getCode());
        GithubProfileResponse githubProfileResponse =
            githubClient.getGithubProfileFromGithub(githubAccessToken);
        Member member = memberService.findMemberByEmailOrCreate(githubProfileResponse.getEmail());
        String token = tokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

}
