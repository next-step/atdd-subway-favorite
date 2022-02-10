package nextstep.auth.model.authentication.service;

import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberRepository;
import nextstep.utils.exception.MemberException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberAdaptor loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberException::new);
        return MemberAdaptor.of(member);
    }
}
