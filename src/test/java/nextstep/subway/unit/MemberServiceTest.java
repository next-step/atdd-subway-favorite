package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberServiceTest extends SpringTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("사용자 정보 가져오기, 만약 없다면 소셜 로그인 유저로 생성하기")
    @Test
    void getMemberOrCreate() {
        // when
        memberService.getMemberOrCreate("email@email.com");

        // then
        final Member member = memberService.getMember("email@email.com");
        assertThat(member.getEmail()).isEqualTo("email@email.com");
        assertThat(member.isSocialUser()).isTrue();
    }
}
