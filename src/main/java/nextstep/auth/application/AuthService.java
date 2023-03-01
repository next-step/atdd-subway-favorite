package nextstep.auth.application;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse getToken(String email, String password) {
        Member member = memberService.findByEmailAndPassword(email, password);
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
