package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String USER_BASE_URL = "/users";

    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
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
}
