package atdd.path.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JwtTokenProvider.class)
class JwtTokenProviderTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@DisplayName("토큰 생성 후 email 확인")
	@Test
	void createToken() {
		// given
		String email = "abcdefgh@gmail.com";

		// when
		String token = jwtTokenProvider.createToken(email);

		// then
		assertThat(token).isNotNull();
		assertThat(token).isNotEqualTo(email);
		assertThat(jwtTokenProvider.getUserEmail(token)).isEqualTo(email);
	}
}