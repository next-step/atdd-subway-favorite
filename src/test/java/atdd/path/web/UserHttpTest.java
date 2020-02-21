package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public Long createUser(String EMAIL_IN_REQUEST, String NAME_IN_REQUEST, String PWD_IN_REQUEST) {
        CreateUserRequestView request = new CreateUserRequestView(EMAIL_IN_REQUEST, NAME_IN_REQUEST, PWD_IN_REQUEST);
        return webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateUserRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(UserResponseView.class)
                .getResponseBody()
                .toStream()
                .map(UserResponseView::getId)
                .collect(Collectors.toList())
                .get(0);
    }
}
