package nextstep.member.application;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.service.UserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import static nextstep.auth.authentication.AuthenticationException.NOT_FOUND_EMAIL;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(NOT_FOUND_EMAIL));

        return LoginMember.of(member);
    }
}
