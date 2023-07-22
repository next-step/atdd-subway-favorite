package nextstep.api.member.application.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.AuthenticationException;
import nextstep.api.auth.application.userdetails.UserDetails;
import nextstep.api.auth.application.userdetails.UserDetailsService;
import nextstep.api.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final var member = memberRepository.findByEmail(username).orElseThrow(AuthenticationException::new);
        return new CustomUserDetails(member.getEmail(), member.getPassword(), member.getRole());
    }
}
