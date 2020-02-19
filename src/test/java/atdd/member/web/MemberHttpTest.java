package atdd.member.web;

import static atdd.member.MemberConstant.MEMBER_BASE_URL;

import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.MemberResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class MemberHttpTest {

    public WebTestClient webTestClient;

    public MemberHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<MemberResponseView> join(CreateMemberRequestView view) {
        return webTestClient.post().uri(MEMBER_BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(view), CreateMemberRequestView.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader().exists("Location")
            .expectBody(MemberResponseView.class)
            .returnResult();
    }


}
