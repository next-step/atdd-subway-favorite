package nextstep.auth.ui;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("유저 정보(UserDetails)")
class UserDetailsTest {

    @DisplayName("유저 정보의 값을 조회할 수 있다")
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

        assertAll(
                () -> assertThat(userDetails.getId()).isEqulaTo(id),
                () -> assertThat(userDetails.getEmail()).isEqulaTo(email),
                () -> assertThat(userDetails.getPassword()).isEqulaTo(password),
                () -> assertThat(userDetails.getAge()).isEqulaTo(age)
        );
    }
}
