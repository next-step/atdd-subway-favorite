package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.TestConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {

    public static final String LOGIN_URL = "/login";

    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("로그인")
    @Test
    void loginTest() {
        //given
        userHttpTest.createUserTest(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN);

        // when, then
        String request = "{\"email\":\"" + TestConstant.EMAIL_BROWN + "\"," +
                "\"password\":\"" + TestConstant.PASSWORD_BROWN + "\"}";

        webTestClient.post().uri(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.tokenType").isEqualTo("bearer");
    }
}
