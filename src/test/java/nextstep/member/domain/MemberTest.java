package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    public static final String EMAIL = "eamil@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String UPDATED_EMAIL = "update@email.com";
    public static final String UPDATED_PASSWORD = "updatePassword";
    public static final int UPDATED_AGE = 10;
    public static final String WRONG_PASSWORD = "wrongPassword";

    @DisplayName("멤버 정보를 업데이트 한다")
    @Test
    void update() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Member updatedMember = new Member(UPDATED_EMAIL, UPDATED_PASSWORD, UPDATED_AGE);

        // when
        member.update(updatedMember);

        // then
        Assertions.assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(UPDATED_EMAIL),
                () -> assertThat(member.getAge()).isEqualTo(UPDATED_AGE),
                () -> assertThat(member.getPassword()).isEqualTo(UPDATED_PASSWORD)
        );
    }

    @DisplayName("멥버 비번을 확인한다")
    @Test
    void checkPassword() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);

        // when
        boolean rightPassword = member.checkPassword(PASSWORD);
        boolean wrongPassword = member.checkPassword(WRONG_PASSWORD);

        // then
        Assertions.assertAll(
                () -> assertThat(rightPassword).isTrue(),
                () -> assertThat(wrongPassword).isFalse()
        );
    }
}
