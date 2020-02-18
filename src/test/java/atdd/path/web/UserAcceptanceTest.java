package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    @DisplayName("유저_회원가입이_성공하는지")
    @Test
    public void userSighUp() {
        String email = "sgkim94@github.com";
        String name = "김상구";

        webTestClient.post().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo(email)
                .jsonPath("$.name").isEqualTo(name);
    }
}
