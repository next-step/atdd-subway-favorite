package nextstep.auth.ui;

import nextstep.auth.UserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.auth.util.AuthFixture.DEFAULT_PASSWORD;
import static nextstep.auth.util.AuthFixture.createUserDetails;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유저 정보(UserDetails)")
class UserDetailsTest {

    @DisplayName("유저 정보의 비밀번호가 맞는지 검증한다")
    @Test
    void checkPassword() {
        // given
        final UserDetails userDetails = createUserDetails(DEFAULT_PASSWORD);

        // when and then
        assertThat(userDetails.checkPassword(DEFAULT_PASSWORD)).isTrue();
    }
}
