package subway.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.auth.userdetails.UserDetails;
import subway.auth.userdetails.UserDetailsService;
import subway.constant.SubwayMessage;
import subway.exception.AuthenticationException;
import subway.member.domain.CustomUserDetails;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;

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
