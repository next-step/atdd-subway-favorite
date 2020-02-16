package atdd.path.web;

import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class UserAcceptanceTest {

    @Autowired
    public WebTestClient webTestClient;

    private EntityExchangeResult<UserResponseView> createUser() {

        final String inputString = "{\"email\":\"boorwonie@email.com\",\"이름\":\"최현진\",\"password\":\"nextstep\"}";

        return webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputString), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(UserResponseView.class)
                .returnResult();
    }

    @Test
    void createUserTest() {
        createUser();
    }

    @Test
    void deleteUserTest() {

        String path = createUser().getResponseHeaders().getLocation().getPath();

        webTestClient.delete().uri(path)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.delete().uri(path)
                .exchange()
                .expectStatus().isNotFound();

    }
}
