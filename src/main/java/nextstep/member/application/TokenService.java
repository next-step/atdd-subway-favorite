package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.presentation.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
