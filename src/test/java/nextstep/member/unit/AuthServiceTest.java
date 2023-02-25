package nextstep.member.unit;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.GithubLoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.common.constants.ErrorConstant.INVALID_EMAIL_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("토큰 생성 실패-비밀번호 미일치")
    void login_mismatchPwd() {
        // given
        final TokenRequest request = new TokenRequest(EMAIL, "other");

        // when
        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_EMAIL_PASSWORD);
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

    @ParameterizedTest
    @ValueSource(strings = {"832ovnq039hfjn", "mkfo0aFa03m", "m-a3hnfnoew92", "nvci383mciq0oq"})
    @DisplayName("깃허브 로그인")
    void githubLogin(final String code) {
        // when
        final TokenResponse token = authService.oauth2Login(new GithubLoginRequest(code));

        // then
        assertThat(token.getAccessToken()).isNotBlank();
    }
}
