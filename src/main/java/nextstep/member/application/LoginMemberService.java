package nextstep.member.application;

import nextstep.auth.filter.LoginService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements LoginService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }

    public boolean isUserExist(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
