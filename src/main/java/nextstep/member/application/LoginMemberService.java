package nextstep.member.application;

import nextstep.auth.userdetails.User;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String email) {
        final Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new User(member.getEmail(), member.getPassword(), member.getRoles());
    }

}
