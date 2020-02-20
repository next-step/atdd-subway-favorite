package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.domain.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class UserHttpTest {
    final String ACCESS_TOKEN_HEADER = "Authorization";

    final String USER_PATH = "/users";

    final ObjectMapper mapper = new ObjectMapper();
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<User> createUserRequest(CreateUserRequestView view, final String accessToken) {
        String inputJson = writeValueAsString(view);

        return webTestClient.post().uri(USER_PATH)
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(User.class)
                .returnResult();
    }

    public void deleteUserRequest(final long id, final String accessToken) {
        webTestClient.delete().uri(USER_PATH + "/" + id)
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .exchange()
                .expectStatus().isNoContent();
    }

    public EntityExchangeResult loginRequest(LoginRequestView view) {
        String inputJson = writeValueAsString(view);

        return webTestClient.post().uri(USER_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().returnResult();
    }

    private String writeValueAsString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
