package nextstep.subway.application;

import nextstep.auth.model.authentication.UserDetails;
import nextstep.auth.model.authentication.service.UserDetailsService;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.subway.domain.member.MemberRepository;
import nextstep.utils.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(AuthenticationException::new);
        return MemberAdaptor.of(member);
    }
}
