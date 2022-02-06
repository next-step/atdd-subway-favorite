package nextstep.domain.member.service;

import nextstep.domain.member.domain.LoginMember;
import nextstep.domain.member.domain.Member;
import nextstep.domain.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LegacyUserDetailsService {
    private MemberRepository memberRepository;

    public LegacyUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
