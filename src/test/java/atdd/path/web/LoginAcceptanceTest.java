package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {
    public static final String LOGIN_BASE_URI = "/login";
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    public void 로그인_요청하기() {
        //given
        Long userId = userHttpTest.createUser(EMAIL, NAME, PASSWORD);

        //when
        webTestClient.post().uri(LOGIN_BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.tokenType").isNotEmpty();
    }
}
