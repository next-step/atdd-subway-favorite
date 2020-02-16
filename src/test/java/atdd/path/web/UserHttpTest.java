package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<UserResponseView> createUserRequest() {
        CreateUserRequestView createUserRequestView = CreateUserRequestView.builder()
                .email("boorwonie@email.com")
                .name("브라운")
                .password("subway")
                .build();

        return webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(createUserRequestView), CreateUserRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(UserResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<UserResponseView> retrieveStationRequest(Long userId) {
        return webTestClient.get().uri("/users/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseView.class)
                .returnResult();
    }

    public Long createUser() {
        EntityExchangeResult<UserResponseView> stationResponse = createUserRequest();
        return stationResponse.getResponseBody().getId();
    }

    public EntityExchangeResult<UserResponseView> retrieveStation(Long userId) {
        return retrieveStationRequest(userId);
    }
}
