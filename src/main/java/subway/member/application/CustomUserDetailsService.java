package subway.member.application;

import lombok.RequiredArgsConstructor;
import subway.constant.SubwayMessage;
import subway.exception.AuthenticationException;
import subway.auth.userdetails.UserDetails;
import subway.auth.userdetails.UserDetailsService;
import subway.member.domain.CustomUserDetails;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new AuthenticationException(SubwayMessage.MEMBER_NOT_FOUND));
        return CustomUserDetails.from(member);
    }
}
