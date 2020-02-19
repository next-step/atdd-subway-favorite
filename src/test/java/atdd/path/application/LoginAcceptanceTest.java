package atdd.path.application;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.CreateAccessTokenRequestView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {

	@DisplayName("로그인 요청 및 토큰 발급 받기")
	@Test
	void login() {
		// given
		CreateAccessTokenRequestView request =
			CreateAccessTokenRequestView.of("starkying@gmail.com", "12345678#$");

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
}
