package nextstep.member.application;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import static nextstep.auth.authentication.AuthenticationException.NOT_FOUND_EMAIL;

@Service
public class CustomUserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(NOT_FOUND_EMAIL));

        return LoginMember.of(member);
    }
}
