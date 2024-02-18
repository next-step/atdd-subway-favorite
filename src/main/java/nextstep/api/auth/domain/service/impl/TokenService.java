package nextstep.api.auth.domain.service.impl;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.domain.operators.JwtTokenProvider;
import nextstep.api.member.application.MemberService;
import nextstep.common.exception.member.AuthenticationException;
import nextstep.api.auth.application.dto.TokenResponse;
import nextstep.api.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(String email) {
        return new TokenResponse(jwtTokenProvider.createToken(email));
    }

    @Deprecated
    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

}
