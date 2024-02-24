package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.TokenResponse;
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
            throw new AuthenticationException("인증 정보가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
