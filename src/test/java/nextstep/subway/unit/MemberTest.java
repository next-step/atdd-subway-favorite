package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.exception.BadCredentialException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "testpassword";
    private static final int AGE = 20;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("유저 정보를 수정한다.")
    @Test
    void updateMember() {
        final Member updateMember = new Member(EMAIL + "update", PASSWORD + "update", AGE + 10);
        member.update(updateMember);

        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(updateMember.getEmail()),
                () -> assertThat(member.getPassword()).isEqualTo(updateMember.getPassword()),
                () -> assertThat(member.getAge()).isEqualTo(updateMember.getAge())
        );
    }

    @DisplayName("올바른 비밀번호로 비밀번호 검사를 하면 아루일도 발생하지 않는다.")
    @Test
    void checkPasswordWithCorrectPassword() {
        assertDoesNotThrow(() -> member.checkPassword(PASSWORD));
    }

    @DisplayName("잘못된 비밀번호로 비밀번호 검사를 하면 오류가 발생한다.")
    @Test
    void checkPasswordWithWrongPassword() {
        assertThatThrownBy(() -> member.checkPassword(PASSWORD + "wrong"))
                .isInstanceOf(BadCredentialException.class);
    }
}
