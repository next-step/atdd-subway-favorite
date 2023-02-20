package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.MemberAuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;

@DisplayName("인증 서비스 테스트")
@SpringBootTest
@Transactional
class AuthServiceTest {

	@Autowired private MemberAuthService memberAuthService;

	private String EMAIL = "admin@email.com";
	private String PASSWORD = "password";

	private TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
	private TokenRequest failtokenRequest = new TokenRequest(EMAIL, "testPassword");

	@DisplayName("정상적인 토근 생성")
	@Test
	void successCreateToken() {
		TokenResponse token = memberAuthService.createMemberToken(tokenRequest);
		assertThat(token.getAccessToken()).isNotNull();
	}

	@DisplayName("토큰 생성 실패")
	@Test
	void failCreateToken() {
		assertThrows(IllegalArgumentException.class, () -> memberAuthService.createMemberToken(failtokenRequest));
	}
}

