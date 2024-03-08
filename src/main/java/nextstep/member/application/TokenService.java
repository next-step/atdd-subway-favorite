package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider,
        GithubClient githubClient) {
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

    public String githubLogin(String code) {
        String accessToken = githubClient.requestGithubToken(code);
        GithubProfileResponse response = githubClient.findUserInfo(accessToken);
        try {
            memberService.findMemberByEmail(response.getMail());
        }catch(RuntimeException e) {
            memberService.createMember(new MemberRequest(response.getMail(), response.getMail() + "123!", response.getAge()));
        }

        return accessToken;
    }
}
