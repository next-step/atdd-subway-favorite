package nextstep.support;

import static nextstep.Fixtures.aMember;

import nextstep.member.domain.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class DataLoader {
  private final MemberRepository memberRepository;

  public DataLoader(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public void loadData() {
    memberRepository.save(aMember().build());
  }
}
