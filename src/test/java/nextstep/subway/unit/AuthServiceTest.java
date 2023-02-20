package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.auth.AuthRequest;
import nextstep.auth.AuthResponse;
import nextstep.auth.AuthService;

@DisplayName("인증 서비스 테스트")
@SpringBootTest
@Transactional
class AuthServiceTest {

	@Autowired private AuthService authService;

	private String EMAIL = "admin@email.com";
	private String PASSWORD = "password";

	private AuthRequest authRequest = new AuthRequest(EMAIL, PASSWORD);
	private AuthRequest failAuthRequest = new AuthRequest(EMAIL, "testPassword");

	@DisplayName("정상적인 토근 생성")
	@Test
	void successCreateToken() {
		AuthResponse token = authService.createToken(authRequest);
		assertThat(token.getAccessToken()).isNotNull();
	}

	@DisplayName("토큰 생성 실패")
	@Test
	void failCreateToken() {
		assertThrows(IllegalArgumentException.class, () -> authService.createToken(failAuthRequest));
	}
}

