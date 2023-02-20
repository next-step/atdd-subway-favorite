package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        final Member member = memberService.authenticate(tokenRequest);
        final String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(token);
    }

}
