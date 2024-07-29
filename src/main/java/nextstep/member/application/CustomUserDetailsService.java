package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetailsService;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.exception.UsernameNotFoundException;
import nextstep.member.domain.CustomUserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member =
        memberRepository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    return new CustomUserDetails(member.getEmail(), member.getPassword());
  }
}
