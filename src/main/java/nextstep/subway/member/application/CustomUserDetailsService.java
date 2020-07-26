package nextstep.subway.member.application;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }

    @Override
    public UserDetails convertJsonToUserDetail(String payload) throws Exception {
        MemberResponse memberResponse = ObjectMapperUtils.convert(payload, MemberResponse.class);
        return new LoginMember(memberResponse.getId(), memberResponse.getEmail(), null, memberResponse.getAge());
    }
}
