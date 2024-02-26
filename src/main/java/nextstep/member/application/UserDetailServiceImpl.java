package nextstep.member.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.UserDetail;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailService {

  private final MemberService memberService;

  @Override
  public Optional<UserDetail> findByEmail(String email) {
    return memberService.findMemberByEmail(email)
        .map(member -> new UserDetail(member.getEmail(), member.getPassword(), member.getAge()));
  }

  @Override
  public UserDetail createUser(String email, String password, int age) {
    final var member = memberService.createMember(email, password, age);
    return new UserDetail(member.getEmail(), member.getPassword(), member.getAge());
  }
}
