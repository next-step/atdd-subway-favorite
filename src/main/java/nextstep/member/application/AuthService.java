package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final GithubClient githubClient;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService, GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.githubClient = githubClient;
    }

    @Transactional(readOnly = true)
    public TokenResponse createToken(TokenRequest request) {
        Member findMember = memberService.findByEmail(request.getEmail());
        findMember.checkPassword(request.getPassword());

        return TokenResponse.of(jwtTokenProvider.createToken(findMember.getEmail()));
    }

    @Transactional
    public TokenResponse createTokenWithGithub(GithubTokenRequest request) {
        String accessToken = githubClient.getAccessTokenFromGithub(request.getCode());
        GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(accessToken);
        Member member = memberService.findOrCreateMember(githubProfileResponse.getEmail(), request.getCode());

        return TokenResponse.of(jwtTokenProvider.createToken(member.getEmail()));
    }
}
