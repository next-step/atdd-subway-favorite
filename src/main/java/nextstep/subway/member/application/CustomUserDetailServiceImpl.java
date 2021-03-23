package nextstep.subway.member.application;

import nextstep.subway.auth.application.CustomUserDetailsService;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new UserDetails.Builder().id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .age(member.getAge())
                .build();
    }
}
