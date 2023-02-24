package nextstep.login.application;

import nextstep.login.application.dto.LoginResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(final String email, final String password) {
        Member findMember = memberService.findMemberByEmail(email);
        findMember.validatePassword(password);

        return new LoginResponse(jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles()));
    }
}
