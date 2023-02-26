package nextstep.member.application;

import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;


    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse authByBasic(String email, String password) {
        Member member = memberService.authenticate(email, password);
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse authByGithub(String code) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);

        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        Member member = memberService.findMemberOrElseJoin(githubProfile.getEmail());

        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());

        return new TokenResponse(token);
    }
}
