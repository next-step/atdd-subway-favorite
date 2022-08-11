package nextstep.member.application;

import nextstep.auth.authentication.UserDetailService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginMemberService implements UserDetailService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.map(LoginMember::of).orElse(null);
    }
}
