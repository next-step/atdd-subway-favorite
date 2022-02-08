package nextstep.auth.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("유저 정보(UserDetails)")
class UserDetailsTest {

    @DisplayName("유저 정보의 값을 조회할 수 있다")
    @Test
    void getter() {
        final Long id = 1L;
        final String email = "email@email.com";
        final String password = "password";
        final int age = 1;

        final UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getId()).willReturn(id);
        given(userDetails.getEmail()).willReturn(email);
        given(userDetails.getPassword()).willReturn(password);
        given(userDetails.getAge()).willReturn(age);
        given(userDetails.checkPassword(password)).willReturn(true);

        assertAll(
                () -> assertThat(userDetails.getId()).isEqualTo(id),
                () -> assertThat(userDetails.getEmail()).isEqualTo(email),
                () -> assertThat(userDetails.getPassword()).isEqualTo(password),
                () -> assertThat(userDetails.getAge()).isEqualTo(age),
                () -> assertThat(userDetails.checkPassword(password)).isTrue()
        );
    }
}
