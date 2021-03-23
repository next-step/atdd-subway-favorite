package nextstep.subway.member.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.UserDetail;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.NotExistEmailException;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetail loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NotExistEmailException::new);
        return LoginMember.of(member);
    }
}
