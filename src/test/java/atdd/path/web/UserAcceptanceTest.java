package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserRequestView;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void 회원_가입() {
        UserRequestView userInfo = UserRequestView.builder()
                .email("boorwonie@email.com")
                .name("브라운")
                .password("subway")
                .build();

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userInfo)
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().is2xxSuccessful();
    }
}
