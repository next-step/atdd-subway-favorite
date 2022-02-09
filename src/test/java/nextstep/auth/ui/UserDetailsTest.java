package nextstep.auth.ui;

import nextstep.auth.UserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.auth.util.authFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("유저 정보(UserDetails)")
class UserDetailsTest {

    @DisplayName("유저 정보의 값을 조회할 수 있다")
    @Test
    void getter() {
        final UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getId()).willReturn(DEFAULT_ID);
        given(userDetails.getEmail()).willReturn(DEFAULT_EMAIL);
        given(userDetails.getPassword()).willReturn(DEFAULT_PASSWORD);
        given(userDetails.getAge()).willReturn(DEFAULT_AGE);
        given(userDetails.checkPassword(DEFAULT_PASSWORD)).willReturn(true);

        assertAll(
                () -> assertThat(userDetails.getId()).isEqualTo(DEFAULT_ID),
                () -> assertThat(userDetails.getEmail()).isEqualTo(DEFAULT_EMAIL),
                () -> assertThat(userDetails.getPassword()).isEqualTo(DEFAULT_PASSWORD),
                () -> assertThat(userDetails.getAge()).isEqualTo(DEFAULT_AGE),
                () -> assertThat(userDetails.checkPassword(DEFAULT_PASSWORD)).isTrue()
        );
    }
}
