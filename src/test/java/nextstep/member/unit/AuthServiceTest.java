package nextstep.member.unit;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("토큰 생성 실패-비밀번호 미일치")
    void login_mismatchPwd() {
        // given
        final TokenRequest request = new TokenRequest(EMAIL, "other");

        // when
        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("토큰 생성")
    void login() {
        // given
        final TokenRequest request = new TokenRequest(EMAIL, PASSWORD);

        // when
        TokenResponse token = authService.login(request);

        // then
        assertThat(token.getAccessToken()).isNotBlank();
    }
}
