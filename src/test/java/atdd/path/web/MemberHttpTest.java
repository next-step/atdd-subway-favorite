package atdd.path.web;

import atdd.path.application.dto.CreateMemberRequestView;
import atdd.path.application.dto.MemberResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class MemberHttpTest {

	public WebTestClient webTestClient;

	public MemberHttpTest(final WebTestClient webTestClient) {
		this.webTestClient = webTestClient;
	}

	public Long createMember(final String email, final String name, final String password) {
		EntityExchangeResult<MemberResponseView> responseView = createMemberRequest(email, name, password);
		return responseView.getResponseBody().getId();
	}

	public EntityExchangeResult<MemberResponseView> createMemberRequest(final String email, final String name, final String password) {
		CreateMemberRequestView createMemberRequestView = new CreateMemberRequestView(email, name, password);
		return webTestClient.post().uri("/members")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(createMemberRequestView), CreateMemberRequestView.class)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectHeader().exists("Location")
			.expectBody(MemberResponseView.class)
			.returnResult();
	}

	public EntityExchangeResult<MemberResponseView> retrieveMember(final Long memberId) {
		return retrieveMemberRequest("/members/" + memberId);
	}

	private EntityExchangeResult<MemberResponseView> retrieveMemberRequest(final String uri) {
		return webTestClient.get().uri(uri)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(MemberResponseView.class)
			.returnResult();
	}
}
