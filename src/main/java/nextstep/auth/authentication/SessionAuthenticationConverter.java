package nextstep.auth.authentication;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class SessionAuthenticationConverter implements AuthenticationConverter {

	@Override
	public AuthenticationToken convert(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		String principal = paramMap.get("username")[0];
		String credentials = paramMap.get("password")[0];

		return new AuthenticationToken(principal, credentials);
	}
}
