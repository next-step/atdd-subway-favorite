package nextstep.member.application;

import nextstep.member.AuthenticationException;
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
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getId());

        return new TokenResponse(token);
    }

    public TokenResponse createToken(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);

        Member member = memberService.findMemberByEmail(githubProfileResponse.getEmail());

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getId());

        return new TokenResponse(token);
    }
}
