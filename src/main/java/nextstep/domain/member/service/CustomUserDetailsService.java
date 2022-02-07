package nextstep.domain.member.service;

import nextstep.auth.authentication.UserDetailsService;
import nextstep.domain.member.domain.LoginMemberImpl;
import nextstep.domain.member.domain.Member;
import nextstep.domain.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public LoginMemberImpl loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMemberImpl.of(member);
    }
}
