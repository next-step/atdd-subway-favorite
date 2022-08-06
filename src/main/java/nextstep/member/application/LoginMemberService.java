package nextstep.member.application;

import nextstep.auth.member.LoginMember;
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
        return LoginMember.of(member.getEmail(), member.getPassword(), member.getRoles());
    }

    public boolean isUserExist(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
