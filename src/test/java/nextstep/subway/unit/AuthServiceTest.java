package nextstep.subway.unit;

import nextstep.auth.application.AuthService;
import nextstep.member.application.dto.JwtTokenResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthServiceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    AuthService authService;

    @DisplayName("이메일이 일치하는 멤버가 존재한다면 토큰반환")
    @Test
    void getToken() {
        //when
        JwtTokenResponse response = authService.getToken(EMAIL, PASSWORD);

        //then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getAccessToken()).isNotBlank();
    }

    @DisplayName("이메일이 일치하지 않는다면 예외 발생")
    @Test
    void getTokenWithEmailException() {
        //given
        String wrongEmail = "wrong@email.com";

        //then
        Assertions.assertThatThrownBy(() -> {
            authService.getToken(wrongEmail, PASSWORD);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("비밀번호가 일치하지 않는다면 예외 발생")
    @Test
    void getTokenWithPasswordException() {
        //given
        String wrongPassword = "wrongPassword";

        //then
        Assertions.assertThatThrownBy(() -> {
            authService.getToken(EMAIL, wrongPassword);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
