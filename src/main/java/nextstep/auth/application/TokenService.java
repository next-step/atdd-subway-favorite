package nextstep.auth.application;

import nextstep.auth.application.dto.TokenResponse;
import nextstep.config.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
