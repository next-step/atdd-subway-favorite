package nextstep.subway.member.application;

import nextstep.subway.auth.infrastructure.UserDetails;
import nextstep.subway.auth.infrastructure.UserDetailsService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return UserDetails.of(member.getId(), member.getEmail(), member.getPassword());
    }
}
