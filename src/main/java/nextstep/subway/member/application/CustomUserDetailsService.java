package nextstep.subway.member.application;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.User;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public User loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
