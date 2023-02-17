package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.member.domain.exception.PasswordMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("멤버 관련 기능")
class MemberTest {

    @DisplayName("멤버 비밀번호가 일치하지 않으면 예외 처리한다.")
    @Test
    void validatePassword() {
        Member member = new Member("admin@gmail.com", "1234", 25);

        assertAll(
                () -> assertThatCode(() -> member.validatePassword("1234"))
                        .doesNotThrowAnyException(),
                () -> assertThatThrownBy(() -> member.validatePassword("1235"))
                        .isInstanceOf(PasswordMismatchException.class)
        );
    }
}
