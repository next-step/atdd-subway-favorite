package nextstep.auth.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class TokenAuthenticationConverter implements AuthenticationConverter {
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";

	@Override
	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		String stringJson = request.getReader()
			.lines()
			.findFirst()
			.orElseThrow(AuthenticationException::new);
		JSONObject jsonObject = new JSONObject(stringJson);

		return new AuthenticationToken(jsonObject.getString(EMAIL), jsonObject.getString(PASSWORD));
	}
}
