package atdd.path.web;

import atdd.path.AbstractHttpTest;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.dto.MemberResponseView;
import atdd.path.domain.Member;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static atdd.path.TestConstant.LOGIN_URL;
import static atdd.path.TestConstant.MEMBER_URL;
import static atdd.path.TestUtils.jsonOf;

public class MemberHttpTest extends AbstractHttpTest {

    public MemberHttpTest(WebTestClient webTestClient) {
        super(webTestClient);
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

        return createRequest(MemberResponseView.class, MEMBER_URL, inputJson);
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
        return webTestClient.post().uri(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoginResponseView.class)
                .returnResult();
    }

}