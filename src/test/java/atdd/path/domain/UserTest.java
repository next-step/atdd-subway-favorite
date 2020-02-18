package atdd.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void delete() {
        // given
        User user = User.of("serverwizard@woowahan.com", "홍종완", "1234");

        // when
        user.delete();

        // then
        assertThat(user.isDeleted()).isTrue();
    }
}
