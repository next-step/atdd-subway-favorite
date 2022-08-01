package nextstep.member.application;

import nextstep.auth.user.LoginUserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginMemberDetailService implements LoginUserDetailsService {
    private final MemberRepository memberRepository;

    public LoginMemberDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Transactional(readOnly = true)
    public LoginMember loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
