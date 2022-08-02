package nextstep.member.application;

import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.authentication.user.User;
import nextstep.auth.authentication.user.UserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements UserDetailsService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new User(email, member.getPassword(), member.getRoles());
    }
}
