package atdd.path.web;

import atdd.path.application.dto.UserRequestView;
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

    public EntityExchangeResult<Long> createUserRequest() {
        UserRequestView userRequestView = UserRequestView.builder()
                .email("boorwonie@email.com")
                .name("브라운")
                .password("subway")
                .build();

        return webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRequestView), UserRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(Long.class)
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
        EntityExchangeResult<Long> stationResponse = createUserRequest();
        return stationResponse.getResponseBody();
    }

    public EntityExchangeResult<UserResponseView> retrieveStation(Long userId) {
        return retrieveStationRequest(userId);
    }
}
