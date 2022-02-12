package nextstep.auth.ui;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.auth.util.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유저 정보 조회 서비스(UserDetailsService)")
class UserDetailsServiceTest {

    @DisplayName("식별자를 통해 유저 정보를 반환 받을 수 있다")
    @Test
    void loadUserByUsername() {
        // given
        final UserDetailsService userDetailsService = createUserDetailsService(DEFAULT_PASSWORD);

        // when
        final UserDetails userDetails = userDetailsService.loadUserByUsername(DEFAULT_EMAIL);

        // then(비밀번호 검증은 해당 클래스 책임 아님 + 단순히 값이 반환되었는지만 확인하면 될 것 같다)
        assertThat(userDetails).isNotNull();
    }
}
