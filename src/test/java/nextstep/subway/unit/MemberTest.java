package nextstep.subway.unit;

import nextstep.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {
    @Test
    @DisplayName("password 비교 테스트 : 정상")
    void test1() {
        Member member = new Member("admin@email.com", "password", 3);
        ReflectionTestUtils.setField(member, "id", 1L);
        assertThat(member.arePasswordsSame("password")).isTrue();
    }

    @Test
    @DisplayName("password 비교 테스트 : 실패")
    void test2() {
        Member member = new Member("admin@email.com", "password", 3);
        ReflectionTestUtils.setField(member, "id", 1L);
        assertThat(member.arePasswordsSame("password")).isTrue();
    }

}
