package nextstep.domain.member.service;

import nextstep.domain.member.domain.LoginMember;
import nextstep.domain.member.domain.MemberRepository;
import nextstep.domain.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
