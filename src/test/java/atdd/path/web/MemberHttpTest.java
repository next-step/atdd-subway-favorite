package atdd.path.web;

import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.dto.MemberResponseView;
import atdd.path.domain.Member;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static atdd.path.TestUtils.jsonOf;

public class MemberHttpTest {

    private WebTestClient webTestClient;

    public MemberHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public MemberResponseView createMember(Member member) {
        EntityExchangeResult<MemberResponseView> result = createMemberRequest(member);
        return result.getResponseBody();
    }

    public EntityExchangeResult<MemberResponseView> createMemberRequest(Member member) {
        Map<String, Object> map = Map.ofEntries(
                Map.entry("email", member.getEmail()),
                Map.entry("name", member.getName()),
                Map.entry("password", member.getPassword())
        );

        String inputJson = jsonOf(member);

        return webTestClient.post().uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(MemberResponseView.class)
                .returnResult();
    }

    public String loginMember(Member member) {
        EntityExchangeResult<LoginResponseView> result = loginMemberRequest(member);
        LoginResponseView view = result.getResponseBody();
        return view.getTokenType() + " " + view.getAccessToken();
    }

    public EntityExchangeResult<LoginResponseView> loginMemberRequest(Member member) {
        Map<String, Object> map = Map.ofEntries(
                Map.entry("email", member.getEmail()),
                Map.entry("password", member.getPassword())
        );

        String inputJson = jsonOf(map);

        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoginResponseView.class)
                .returnResult();
    }

}