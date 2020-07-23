package nextstep.subway.member.application;

import nextstep.subway.auth.application.UserDetail;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.util.ConvertUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail convertJsonToUserDetail(String json) {
        final MemberResponse memberResponse = ConvertUtils.convertJson2Object(json, MemberResponse.class);
        return new LoginMember(memberResponse.getId(), memberResponse.getEmail(), null, memberResponse.getAge());
    }

    @Override
    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
