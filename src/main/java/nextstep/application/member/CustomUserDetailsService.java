package nextstep.application.member;

import nextstep.domain.member.LoginMember;
import nextstep.domain.member.Member;
import nextstep.domain.member.MemberRepository;
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
