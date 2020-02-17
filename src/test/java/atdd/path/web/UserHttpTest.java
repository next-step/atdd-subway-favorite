package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static atdd.path.application.base.BaseUriConstants.USER_BASE_URL;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    private EntityExchangeResult<UserResponseView> createUserRequest(String email, String name, String password) {
        CreateUserRequestView inputUser = new CreateUserRequestView(email, name, password);
        return webTestClient.post().uri(USER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputUser), CreateUserRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(UserResponseView.class)
                .returnResult();
    }

    public Long createUser(String email, String name, String password) {
        EntityExchangeResult<UserResponseView> userResponse = createUserRequest(email, name, password);
        return userResponse.getResponseBody().getId();
    }

    public EntityExchangeResult<UserResponseView> retrieveUser(Long userId) {
        return retrieveUserRequest(USER_BASE_URL + "/" + userId);
    }

    private EntityExchangeResult<UserResponseView> retrieveUserRequest(String uri) {
        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseView.class)
                .returnResult();
    }
}
