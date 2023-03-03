package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.LoginService;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.subway.utils.GithubResponses;

@SpringBootTest
@Transactional
public class LoginServiceTest {
	@Autowired
	private LoginService loginService;

	@DisplayName("github code(권한증서)로 로그인을 하면 Access Token 을 반환한다.")
	@Test
	void createTokenByGithubToken() {
		// When
		String accessToken = loginService.createTokenByGithubToken(
			new GithubLoginRequest(GithubResponses.사용자1.getCode())
		).getAccessToken();

		// Then
		assertThat(accessToken).isNotBlank();
	}
}
