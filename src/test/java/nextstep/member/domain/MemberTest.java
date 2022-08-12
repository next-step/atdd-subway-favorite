package nextstep.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("아이디가 동일한지 비교한다.")
    void isSameId() {
        // when
        Member member = new Member(1L, "member@email.com", "password", 20, null);

        // then
        assertAll(() -> {
            assertThat(member.isSameId(1L)).isTrue();
            assertThat(member.isSameId(2L)).isFalse();
        });
    }
}