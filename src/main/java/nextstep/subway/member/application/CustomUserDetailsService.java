package nextstep.subway.member.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public LoginMember loadUserByUserName(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
