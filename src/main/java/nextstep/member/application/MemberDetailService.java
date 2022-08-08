package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.login.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public LoginMember loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
