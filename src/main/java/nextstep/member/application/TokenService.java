package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.TokenResponse;
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
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());

        return new TokenResponse(token);
    }
}
