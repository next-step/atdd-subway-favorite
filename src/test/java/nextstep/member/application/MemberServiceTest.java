package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.function.Supplier;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.FixtureUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  MemberService memberService;

  @DisplayName("멤버 조회 성공")
  @Test
  void 멤버_조회_성공() {
    // given
    var 등록되어_있는_멤버 = memberRepository.save(FixtureUtil.getFixture(Member.class));
    Supplier<RuntimeException> supplier = () -> new RuntimeException("멤버 조회 실패");

    // when
    var result = memberService.findMemberOrElseThrow(등록되어_있는_멤버.getEmail(), supplier);

    // then
    assertThat(result).isEqualTo(등록되어_있는_멤버);
  }

  @DisplayName("멤버 조회 실패, 인자로 받은 supplier 실행")
  @Test
  void 멤버_조회_실패() {
// given
    var 등록되지_않은_이메일 = "email";
    Supplier<RuntimeException> supplier = () -> new RuntimeException("멤버 조회 실패");

    // when
    var result = catchThrowable(() -> memberService.findMemberOrElseThrow(등록되지_않은_이메일, supplier));

    // then
    assertThat(result).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("멤버 조회 실패");
  }
}