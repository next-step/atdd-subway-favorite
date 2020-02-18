package atdd.member.web;

import atdd.member.application.dto.TokenResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class LoginHttpTest
{
    public static final String LOGIN_URL = "/login";
    public WebTestClient webTestClient;

    public LoginHttpTest(WebTestClient webTestClient)
    {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<TokenResponseView> login(String email, String password)
    {
        String inputJson = "{\"email\":\"" + email + "\"," +
                "\"password\":\"" + password + "\"}";

        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(TokenResponseView.class)
                .returnResult();
    }

}
