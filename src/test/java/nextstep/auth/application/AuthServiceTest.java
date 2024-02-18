package nextstep.auth.application;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.common.exception.UnauthorizedException;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    private static final String USER_EMAIL = "email@email.com";
    private static final String USER_PASSWORD = "password";

    @TestConfiguration
    static class AuthTestConfiguration {

        @Bean
        public UserDetailsService userDetailsService() {
            return new MockUserDetailsService(USER_PASSWORD);
        }
    }

    @Test
    @DisplayName("login 를 통해 accessToken 을 발급 받을 수 있다.")
    void loginTest() {
        final AuthResponse code = authService.login(USER_EMAIL, USER_PASSWORD);

        assertThat(code.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("login 시 비밀번호가 틀릴 경우 UnauthorizedException 이 발생한다.")
    void wrongPasswordTest() {
        final String wrongPassword = "wrongPassword";

        assertThatThrownBy(() -> authService.login(USER_EMAIL, wrongPassword))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("github login 을 통해 AuthResponse 를 응답받을 수 있다")
    void githubLoginTest() {
        final GithubResponses githubResponses = GithubResponses.사용자1;

        final AuthResponse authResponse = authService.loginGithub(githubResponses.getCode());

        assertThat(authResponse.getAccessToken()).isNotBlank();
    }

}
