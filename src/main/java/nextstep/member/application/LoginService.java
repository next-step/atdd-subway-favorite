package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.domain.Member;

@Service
public class LoginService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new RuntimeException();
        }

        return jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
    }
}
