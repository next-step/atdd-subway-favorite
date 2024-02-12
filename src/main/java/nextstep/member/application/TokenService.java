package nextstep.member.application;

import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final String email, final String password) {
        final Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        final String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());

        return new TokenResponse(token);
    }
}
