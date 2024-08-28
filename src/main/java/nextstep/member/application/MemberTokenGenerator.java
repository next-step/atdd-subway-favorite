package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberTokenGenerator implements TokenGenerator {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse generateToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }
}
