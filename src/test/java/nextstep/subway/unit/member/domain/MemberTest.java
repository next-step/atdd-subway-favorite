package nextstep.subway.unit.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.member.domain.Member;

@DisplayName("Member Test")
public class MemberTest {
    @Test
    void update() {
        // Given
        Member member = new Member(1L, "username@username.com", "password1234", 10);
        final String CHANGE_EMAIL = "usernam12e@username.com";
        final String CHANGE_PASSWORD = "password164";
        final int CHANGE_AGE = 20;

        // When
        member.update(CHANGE_EMAIL, CHANGE_PASSWORD, CHANGE_AGE);

        // Then
        assertThat(member.getEmail()).isEqualTo(CHANGE_EMAIL);
        assertThat(member.getPassword()).isEqualTo(CHANGE_PASSWORD);
        assertThat(member.getAge()).isEqualTo(CHANGE_AGE);
    }
}
