package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.AccessTokenResponseView;
import atdd.path.application.dto.CreateAccessTokenRequestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static atdd.path.TestConstant.*;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

	private MemberHttpTest memberHttpTest;

	@BeforeEach
	void setUp() {
		memberHttpTest = new MemberHttpTest(webTestClient);
	}

	@DisplayName("내 회원 정보를 요청")
	@Test
	void retrieveMyInfo() {
		// given
		signUpMember();
		String accessToken = getAccessToken(MEMBER_EMAIL, MEMBER_PASSWORD);

		// when & then
		webTestClient.get().uri("/users/me")
			.header("Authorization", "Bearer " + accessToken)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.email").isEqualTo(MEMBER_EMAIL)
			.jsonPath("$.password").isEqualTo(MEMBER_PASSWORD);
	}

	private void signUpMember() {
		memberHttpTest.createMember(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
	}

	private String getAccessToken(final String email, final String password) {
		CreateAccessTokenRequestView request =
			CreateAccessTokenRequestView.of(email, password);

		return webTestClient.post().uri("/oauth/token")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(request), CreateAccessTokenRequestView.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(AccessTokenResponseView.class)
			.returnResult().getResponseBody().getAccessToken();
	}
}
