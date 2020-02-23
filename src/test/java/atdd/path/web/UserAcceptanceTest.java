package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static atdd.path.application.base.BaseUriConstants.USER_BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    private UserHttpTest userHttpTest;
    private LoginHttpTest loginHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.loginHttpTest = new LoginHttpTest(webTestClient);
    }

    @Test
    @DisplayName("회원 가입")
    public void signUpUser() {
        // when
        Long userId = userHttpTest.createUser(USER_EMAIL_1, USER_NAME_1, USER_PASSWORD_1);

        // then
        EntityExchangeResult<UserResponseView> response = userHttpTest.retrieveUser(userId);
        assertThat(response.getResponseBody().getEmail()).isEqualTo(USER_EMAIL_1);
    }

    @Test
    @DisplayName("회원 탈퇴")
    public void deleteUser() {
        // given
        Long userId = userHttpTest.createUser(USER_EMAIL_1, USER_NAME_1, USER_PASSWORD_1);
        EntityExchangeResult<UserResponseView> response = userHttpTest.retrieveUser(userId);

        // when
        webTestClient.delete().uri(USER_BASE_URL + "/" + response.getResponseBody().getId())
                .exchange()
                .expectStatus().isNoContent();

        // then
        webTestClient.get().uri(USER_BASE_URL + "/" + response.getResponseBody().getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * Feature: 회원 정보 조회
     *
     * Scenario: 회원 정보 조회
     * Given 사용자는 회원가입을 했다.
     * And 사용자는 로그인을 통해 액세스 토큰을 가지고 있다.
     * When 사용자는 본인의 정보를 요청한다.
     * Then 사용자는 자신의 정보를 응답받는다.
     */
    @Test
    @DisplayName("회원 정보 조회")
    public void retrieveMyInfo() {
        // given
        Long userId = userHttpTest.createUser(USER_EMAIL_1, USER_NAME_1, USER_PASSWORD_1);
        EntityExchangeResult<UserResponseView> user = userHttpTest.retrieveUser(userId);
        String accessToken = getAccessToken(user.getResponseBody().getEmail(), USER_PASSWORD_1);

        // when
        EntityExchangeResult<UserResponseView> response = webTestClient.get().uri(USER_BASE_URL + "/me")
                .header(JWT_HEADER_AUTHORIZATION, JWT_TOKEN_TYPE + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseView.class)
                .returnResult();

        // then
        assertThat(response.getResponseBody().getId()).isEqualTo(userId);
        assertThat(response.getResponseBody().getEmail()).isEqualTo(USER_EMAIL_1);
    }

    private String getAccessToken(String email, String password) {
        return loginHttpTest.login(email, password).getResponseBody().getAccessToken();
    }
}
