package atdd.path.web;

import atdd.path.application.dto.TokenResponseView;
import atdd.path.domain.User;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static atdd.path.application.base.BaseUriConstants.LOGIN_SIGN_IN_URL;

public class LoginHttpTest {

    private WebTestClient webTestClient;

    public LoginHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<TokenResponseView> login(String email, String password){
        String inputJson = "{\"email\":\"" + email + "\"," +
                "\"password\":\"" + password + "\"}";

        return webTestClient.post().uri(LOGIN_SIGN_IN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(TokenResponseView.class)
                .returnResult();
    }

    public String getAccessTokenFromLogin(User user) {
        return login(user.getEmail(), user.getPassword()).getResponseBody().getAccessToken();
    }
}
