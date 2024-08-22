package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.GithubClient;
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
        Member member = memberService.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원의 요청입니다."));
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenByGithubLogin(String code) {
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubClient.requestGithubToken(code).getAccessToken());

        String token = memberService.findMemberByEmail(githubProfileResponse.getEmail())
                .map(e -> jwtTokenProvider.createToken(e.getEmail()))
                .orElseGet(() -> {
                    MemberResponse memberResponse = memberService.registerOAuthMember(githubProfileResponse.getEmail());
                    return jwtTokenProvider.createToken(memberResponse.getEmail());
                });

        return new TokenResponse(token);
    }
}
