package nextstep.member.application;

import nextstep.auth.member.User;
import nextstep.auth.member.UserDetailService;
import nextstep.auth.member.UserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements UserDetailService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return User.of(member.getEmail(), member.getPassword(), member.getRoles());
    }

    public boolean isUserExist(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
