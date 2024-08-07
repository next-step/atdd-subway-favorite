package nextstep.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.LoginMember;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.domain.UserDetailsService;
import nextstep.auth.exception.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadByUserEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(AuthenticationException::new);

        return new LoginMember(member.getId(), member.getEmail());
    }
}
