package atdd.path.web;

import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.path.web.UserAcceptanceTest.USER_URL;

public class UserHttpTest {

    private WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<UserResponseView> createUserTest(String email, String name, String password) {
        UserRequestView userInfo = UserRequestView.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();

        return webTestClient.post().uri(USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userInfo)
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated()
                .expectBody(UserResponseView.class)
                .returnResult();

    }
}
