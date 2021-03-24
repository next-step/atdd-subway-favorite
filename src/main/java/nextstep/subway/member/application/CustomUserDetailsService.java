package nextstep.subway.member.application;

import nextstep.subway.auth.dto.UserPrincipal;
import nextstep.subway.auth.ui.UserLoader;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserLoader {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserPrincipal loadUserPrincipal(String principal) {
        Member member = memberRepository.findByEmail(principal).orElseThrow(RuntimeException::new);
        return new UserPrincipal(member.getId(), member.getEmail(),member.getAge());
    }
}
