package nextstep.member.unit;

import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {

    @Test
    @DisplayName("비밀번호 확인")
    void checkPassword() {
        // given
        final Member member = new Member("email", "password", 20);

        // when
        // then
        assertThat(member.checkPassword("password")).isTrue();
        assertThat(member.checkPassword("other")).isFalse();
    }
}
