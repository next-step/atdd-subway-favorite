package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<UserResponseView> createUserRequest(User user) {
        CreateUserRequestView createUserRequestView = CreateUserRequestView.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
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

    public EntityExchangeResult<UserResponseView> retrieveUserRequest(Long userId) {
        return webTestClient.get().uri("/users/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseView.class)
                .returnResult();
    }

    public Long createUser(User user) {
        EntityExchangeResult<UserResponseView> stationResponse = createUserRequest(user);
        return stationResponse.getResponseBody().getId();
    }

    public EntityExchangeResult<UserResponseView> retrieveUser(Long userId) {
        return retrieveUserRequest(userId);
    }
}
