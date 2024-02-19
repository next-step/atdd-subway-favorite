package nextstep.fixture;

import java.util.HashMap;
import java.util.Map;

import nextstep.api.auth.application.dto.TokenRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */
public class TokenFixtureCreator {

	public static Map<String, String> createLoginParams(String email, String password) {
		Map<String, String> loginParams = new HashMap<>();
		loginParams.put("email", email);
		loginParams.put("password", password);
		return loginParams;
	}

	public static TokenRequest createTokenRequest(String email, String password) {
		return new TokenRequest(email, password);
	}
}
