package nextstep.auth;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsService  {
    private final MemberRepository memberRepository;

    public UserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public User loadUserByUsername(String username)  {
        Member findMember = memberRepository.findByEmail(username).orElseThrow(AuthenticationException::new);
        return User.from(findMember);
    }
}
