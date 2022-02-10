package nextstep.auth.model.authentication.service;

import nextstep.subway.domain.member.LoginMember;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberRepository;
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
