package nextstep.auth.application;

import nextstep.auth.infra.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(final TokenRequest request) {
        final Member member = memberService.findByEmail(request.getEmail());
        member.validatePassword(request.getPassword());

        final String token = jwtTokenProvider.createToken(request.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
