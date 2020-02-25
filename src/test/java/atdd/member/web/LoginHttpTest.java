package atdd.member.web;

import atdd.member.application.dto.JwtTokenResponse;
import atdd.member.application.dto.LoginMemberRequestView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class LoginHttpTest {

    public WebTestClient webTestClient;

    public LoginHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<JwtTokenResponse> login(LoginMemberRequestView view) {
        return webTestClient.post().uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(view), LoginMemberRequestView.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(JwtTokenResponse.class)
            .returnResult();
    }

    public String getToken(LoginMemberRequestView view) {
        return login(view)
            .getResponseBody()
            .getToken();
    }
}
