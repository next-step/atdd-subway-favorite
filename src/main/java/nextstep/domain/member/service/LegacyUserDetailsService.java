package nextstep.domain.member.service;

import nextstep.domain.member.domain.LoginMemberImpl;
import nextstep.domain.member.domain.Member;
import nextstep.domain.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LegacyUserDetailsService {
    private MemberRepository memberRepository;

    public LegacyUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMemberImpl loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMemberImpl.of(member);
    }
}
