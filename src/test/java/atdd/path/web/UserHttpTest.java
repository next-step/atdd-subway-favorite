package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.domain.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class UserHttpTest {
    final String USER_PATH = "/users";

    final ObjectMapper mapper = new ObjectMapper();
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<User> createUserRequest(CreateUserRequestView view) {
        String inputJson = writeValueAsString(view);

        return webTestClient.post().uri(USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(User.class)
                .returnResult();
    }

    public void deleteUserRequest(final long id) {
        webTestClient.delete().uri(USER_PATH + "/" + id)
                .exchange()
                .expectStatus().isNoContent();
    }

    private String writeValueAsString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        }catch (JsonProcessingException e) {
            return "";
        }
    }
}
