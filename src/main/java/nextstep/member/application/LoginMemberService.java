package nextstep.member.application;

import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import nextstep.auth.user.User;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return User.of(member.getEmail(), member.getPassword(), member.getRoles());
    }
}
