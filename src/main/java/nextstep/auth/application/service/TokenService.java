package nextstep.auth.application.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.application.exception.AuthenticationException;
import nextstep.member.application.service.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
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
