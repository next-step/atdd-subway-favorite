package nextstep.login.application;

import nextstep.login.application.dto.LoginResponse;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(final String email, final String password) {
        Member findMember = findMemberByEmail(email);
        findMember.validatePassword(password);

        return new LoginResponse(jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles()));
    }

    private Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
