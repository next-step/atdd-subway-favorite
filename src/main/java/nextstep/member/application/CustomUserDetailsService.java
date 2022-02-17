package nextstep.member.application;

import nextstep.auth.application.UserDetailsService;
import nextstep.member.domain.UserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return UserDetails.of(member);
    }
}
