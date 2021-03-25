package nextstep.subway.member.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.ui.LoginMemberPort;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements LoginMemberPort {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public LoginMember getLoginMember(String principal) {
        Member member = memberRepository.findByEmail(principal).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword());
    }
}
