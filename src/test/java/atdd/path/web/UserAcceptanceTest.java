package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.assertj.core.api.Assertions.assertThat;
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
        Long userId = userHttpTest.createUser();

        // Then
        EntityExchangeResult<UserResponseView> response = userHttpTest.retrieveStation(userId);
        assertEquals(userId, response.getResponseBody().getId());
    }
}
