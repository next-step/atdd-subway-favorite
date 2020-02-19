package atdd.member.web;

import static atdd.member.MemberConstant.MEMBER_BASE_URL;

import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.JwtTokenResponseView;
import atdd.member.application.dto.LoginMemberRequestView;
import atdd.member.application.dto.MemberResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class LoginHttpTest {

    public WebTestClient webTestClient;

    public LoginHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<JwtTokenResponseView> login(LoginMemberRequestView view) {
        return webTestClient.post().uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(view), LoginMemberRequestView.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(JwtTokenResponseView.class)
            .returnResult();
    }

    public String getToken(LoginMemberRequestView view) {
        return login(view)
            .getResponseBody()
            .getToken();
    }
}
