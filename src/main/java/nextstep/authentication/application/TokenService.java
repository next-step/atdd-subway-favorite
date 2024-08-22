package nextstep.authentication.application;

import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.authentication.application.dto.TokenResponse;
import nextstep.authentication.application.exception.AuthenticationException;
import nextstep.member.application.MemberService;
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
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getId());

        return new TokenResponse(token);
    }

    public TokenResponse createToken(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);

        Member member = memberService.lookUpOrCreateMember(githubProfileResponse);

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getId());

        return new TokenResponse(token);
    }
}
