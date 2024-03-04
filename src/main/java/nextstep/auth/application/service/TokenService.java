package nextstep.auth.application.service;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.application.exception.AuthenticationException;
import nextstep.member.application.service.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
