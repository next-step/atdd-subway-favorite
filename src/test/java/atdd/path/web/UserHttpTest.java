package atdd.path.web;

import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient){
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<UserResponseView> createStationRequest(String name, String password) {
        User user = User.builder()
                .name(name)
                .password(password)
                .build();

        return webTestClient.post().uri("/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(UserResponseView.class)
                .returnResult();
    }

    public Long createUser(String name, String password){
        EntityExchangeResult<UserResponseView> userResponse = createStationRequest(name, password);
        return userResponse.getResponseBody().getId();
    }

}
