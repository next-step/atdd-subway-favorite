package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.UsernameNotFoundException;
import nextstep.member.domain.DefaultUserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.UserDetails;
import nextstep.member.domain.UserDetailsService;

@Service

public class DefaultUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public DefaultUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new DefaultUserDetails(member.getEmail(), member.getPassword());
    }
}
