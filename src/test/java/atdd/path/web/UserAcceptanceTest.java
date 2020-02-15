package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserRequestView;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void 회원_가입() {
        UserRequestView userInfo = UserRequestView.builder()
                .email("boorwonie@email.com")
                .name("브라운")
                .password("subway")
                .build();

        webTestClient.post().uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userInfo), String.class)
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().is2xxSuccessful();
    }
}
