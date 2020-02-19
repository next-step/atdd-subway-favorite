package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.UserTestConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserAcceptanceTest extends AbstractAcceptanceTest {

    public static final String USER_URL = "/users";

    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    public void createUser() {
        // Given

        // When
        Long userId = userHttpTest.createUser(TEST_USER_1);

        // Then
        EntityExchangeResult<UserResponseView> response = userHttpTest.retrieveUser(userId);
        assertEquals(userId, response.getResponseBody().getId());
    }

    @DisplayName("회원 탈퇴")
    @Test
    public void deleteUser() {
        // Given
        Long userId = userHttpTest.createUser(TEST_USER_1); // 조회 성공
        EntityExchangeResult<UserResponseView> response = userHttpTest.retrieveUser(userId);

        // When
        webTestClient.delete().uri("/users/" + userId)  // 삭제
                .exchange()
                .expectStatus().isNoContent();

        // Then
        webTestClient.get().uri("/users/" + userId)     // 조회 실패
                .exchange()
                .expectStatus().isNotFound();
    }
}
