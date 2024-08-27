package nextstep.authentication.unit;

import nextstep.authentication.application.exception.AuthenticationException;
import nextstep.authentication.domain.AuthenticationInformation;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.utils.UserInformation.사용자1;
import static nextstep.utils.UserInformation.사용자2;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("인증 정보 관련 테스트")
class AuthenticationInformationTest {

    @DisplayName("검증 함수는, 입력된 비밀번호와 일치하지 않으면 예외가 발생한다.")
    @Test
    void verificationTest() {
        // given
        AuthenticationInformation authenticationInformation = new AuthenticationInformation(사용자1.getEmail(), 사용자1.getId(), 사용자1.getPassword());

        // when
        ThrowingCallable actual = () -> authenticationInformation.verification(사용자2.getPassword());

        // then
        assertThatThrownBy(actual).isInstanceOf(AuthenticationException.class);
    }
}