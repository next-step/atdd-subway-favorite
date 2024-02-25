package nextstep.core.member.application;

import nextstep.core.member.application.dto.TokenResponse;
import nextstep.core.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }
}
