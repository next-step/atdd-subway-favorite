package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse authorize(TokenRequest request) {
        Member findMember = memberService.findByEmail(request.getEmail());
        findMember.checkPassword(request.getPassword());

        return TokenResponse.of(jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles()));
    }
}
