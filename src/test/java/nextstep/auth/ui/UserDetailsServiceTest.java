package nextstep.auth.ui;

import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.auth.authFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("유저 정보 조회 서비스(UserDetailsService)")
class UserDetailsServiceTest {

    @DisplayName("식별자를 통해 유저 정보를 반환 받는다")
    @Test
    void loadUserByUsername() {
        final UserDetails userDetails = new LoginMember(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_AGE);
        final UserDetailsService userDetailsService = mock(UserDetailsService.class);

        given(userDetailsService.loadUserByUsername(DEFAULT_EMAIL)).willReturn(userDetails);

        final UserDetails actual = userDetailsService.loadUserByUsername(DEFAULT_EMAIL);
        assertThat(actual).isEqualTo(userDetails);
    }
}
