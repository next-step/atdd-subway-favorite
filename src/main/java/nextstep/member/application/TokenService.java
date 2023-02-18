package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;

@Service
public class TokenService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.authenticate(email, password);
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse createGitHubToken(String code) {
        return new TokenResponse("fasdfasd.fasdfasd.fasdfasd");
    }
}
