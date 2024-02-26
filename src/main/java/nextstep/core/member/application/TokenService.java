package nextstep.core.member.application;

import nextstep.core.member.application.dto.TokenResponse;
import nextstep.core.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }

    public TokenResponse createTokenByGithub(String code) {
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(code);
        Member member = memberService.findOrCreate(githubProfileResponse.getEmail());

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }
}
