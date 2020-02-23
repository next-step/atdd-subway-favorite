package atdd.user.web;

import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import atdd.user.application.dto.UserRequestView;
import atdd.user.application.dto.UserResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.user.web.UserAcceptanceTest.USER_URL;

public class UserHttpTest {

    private WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<UserResponseView> createUser(String email, String name, String password) {
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

    public EntityExchangeResult<LoginResponseView> loginUser(String email, String password) {
        LoginRequestView request = LoginRequestView.builder()
                .email(email)
                .password(password)
                .build();

        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponseView.class)
                .returnResult();
    }
}
