package nextstep.member.application;

import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final MemberRepository memberRepository;

  public CustomUserDetailsService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public LoginMember loadUserByUsername(String email) {
    Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    return LoginMember.of(member);
  }
}
