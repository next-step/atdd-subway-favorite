package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.GithubClient;
import nextstep.member.domain.Member;
import nextstep.member.domain.dto.GithubProfileResponse;
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

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubAccessToken(String code) {
        String token = githubClient.getAccessToken(code);
        return new TokenResponse(token);
    }

    public TokenResponse requestProfile(String accessToken) {
        GithubProfileResponse githubProfileResponse = githubClient.requestProfile(accessToken);
        try {
            Member member = memberService.findMemberByEmail(githubProfileResponse.getEmail());
            return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
        } catch (RuntimeException e) {
            memberService.createMember(new MemberRequest(githubProfileResponse.getEmail(), null, null));
            return new TokenResponse(jwtTokenProvider.createToken(githubProfileResponse.getEmail()));
        }
    }
}
