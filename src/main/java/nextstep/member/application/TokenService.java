package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.login(email, password);

        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);

        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        Member member = memberService.createOrFindMember(githubProfile.getEmail());

        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());

        return new TokenResponse(token);
    }
}
