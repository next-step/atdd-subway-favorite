package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.TokenResponseView;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class LoginAcceptanceTest extends AbstractAcceptanceTest {

    private LoginHttpTest loginHttpTest;
    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.loginHttpTest = new LoginHttpTest(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    /**
     * Feature: 로그인
     *
     * Scenario: 자신의 정보를 통해 로그인
     * Given 사용자는 회원 가입을 했다.
     * When 사용자는 자신의 정보를 통해 로그인을 요청한다.
     * Then 사용자는 본인임을 인증할 수 있는 정보를 응답받는다.
     */
    @Test
    @DisplayName("로그인을 요청하여 토큰을 응답 받기")
    public void login() {
        // given
        Long userId = userHttpTest.createUser(TEST_USER);
        EntityExchangeResult<UserResponseView> user = userHttpTest.retrieveUser(userId);

        // when
        EntityExchangeResult<TokenResponseView> response = loginHttpTest.login(
                user.getResponseBody().getEmail(), USER_PASSWORD_1
        );

        // then
        assertThat(response.getResponseBody().getAccessToken()).isNotEmpty();
        assertThat(response.getResponseBody().getTokenType()).isNotEmpty();
    }
}