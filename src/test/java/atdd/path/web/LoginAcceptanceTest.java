package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.CreateAccessTokenRequestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static atdd.path.TestConstant.*;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {

	private MemberHttpTest memberHttpTest;

	@BeforeEach
	void setUp() {
		memberHttpTest = new MemberHttpTest(webTestClient);
	}

	@DisplayName("로그인 요청 및 토큰 발급 받기")
	@Test
	void login() {
		// given
		signUpMember();
		CreateAccessTokenRequestView request = CreateAccessTokenRequestView.of(MEMBER_EMAIL, MEMBER_PASSWORD);

		// when & then
		webTestClient.post().uri("/oauth/token")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(request), CreateAccessTokenRequestView.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.accessToken").isNotEmpty();
	}

	private void signUpMember() {
		memberHttpTest.createMember(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
	}
}
