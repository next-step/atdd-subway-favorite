package nextstep.member.infra;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
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
    public UserDetails loadUserByPrincipal(String principal) {
        Member member = memberRepository.findByEmail(principal)
                .orElseThrow(AuthenticationException::new);
        return new LoginMember(member);
    }
}
