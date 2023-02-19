package nextstep.auth.application;

import nextstep.auth.config.JwtTokenProvider;
import nextstep.auth.dto.TokenRequest;
import nextstep.auth.dto.TokenResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse login(TokenRequest tokenRequest) {
        final Member member = memberService.findMemberByEmail(tokenRequest.getEmail());

        member.validatePassword(tokenRequest.getPassword());

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
