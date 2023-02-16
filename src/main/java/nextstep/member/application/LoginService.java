package nextstep.member.application;

import nextstep.member.auth.JwtTokenProvider;
import nextstep.member.domain.Member;
import nextstep.member.exception.PasswordAuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }


    public String createToken(String email, String password) {

        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new PasswordAuthenticationException();
        }

        return jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
    }
}
