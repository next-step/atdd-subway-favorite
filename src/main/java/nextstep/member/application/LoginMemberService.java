package nextstep.member.application;

import nextstep.auth.User;
import nextstep.auth.UserDetailsService;
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
    public User loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return User.of(member.getId(), member.getPassword(), member.getRoles());
    }
}
