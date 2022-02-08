package nextstep.auth.ui;

import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("유저 정보 조회 서비스(UserDetailsService)")
class UserDetailsServiceTest {

    @DisplayName("식별자를 통해 유저 정보를 반환 받는다")
    @Test
    void loadUserByUsername() {
        final Long id = 1L;
        final String email = "email@email.com";
        final String password = "password";
        final int age = 1;

        final UserDetails userDetails = new LoginMember(id, email, password, age);
        final UserDetailsService userDetailsService = mock(UserDetailsService.class);

        given(userDetailsService.loadUserByUsername(email)).willReturn(userDetails);

        final UserDetails actual = userDetailsService.loadUserByUsername(email);
        assertThat(actual).isEqualTo(userDetails);
    }
}
