package nextstep.member.auth;

import lombok.AllArgsConstructor;
import nextstep.auth.application.UserDetailsService;
import nextstep.auth.domain.UserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmailOrElseThrow(username);
        return new MemberUserDetails(member);
    }
}
