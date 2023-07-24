package subway.member.application;

import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new AuthenticationException(9999L, "회원 정보가 존재하지 않습니다."));  // TODO: constant
        return CustomUserDetails.from(member);
    }
}
