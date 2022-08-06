package nextstep.member.application;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.user.User;
import nextstep.user.UserDetails;
import nextstep.user.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements UserDetailsService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElse(null);

        if (member == null) {
            return null;
        }

        return new User(member.getEmail(), member.getPassword(), member.getRoles());
    }
}
