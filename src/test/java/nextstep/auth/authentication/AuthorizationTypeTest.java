package nextstep.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthorizationType test")
class AuthorizationTypeTest {

	@ParameterizedTest(name = "소문자 변형 메서드 테스트")
	@CsvSource({"BASIC, basic", "BEARER, bearer"}) // given
	void toLowerCase(AuthorizationType testType, String result) {
		// when
		String lowerCase = testType.toLowerCase();
		// then
		assertThat(lowerCase).isEqualTo(result);
	}
}