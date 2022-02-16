package nextstep.member.application;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.PasswordCheckableUser;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements PasswordCheckableUserService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public PasswordCheckableUser loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(AuthenticationException::new);
        return LoginMember.of(member);
    }
}
