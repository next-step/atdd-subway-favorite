package nextstep.auth.authentication;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.service.CustomUserDetails;

public class UsernamePasswordAuthenticationFilter extends AuthenticationInterceptor {
	private static final String PRINCIPAL_NAME = "username";
	private static final String CREDENTIAL_NAME = "password";

	public UsernamePasswordAuthenticationFilter(CustomUserDetails customUserDetails) {
		super(customUserDetails);
	}

	@Override
	public AuthenticationToken convert(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String userName = parameterMap.get(PRINCIPAL_NAME)[0];
		String password = parameterMap.get(CREDENTIAL_NAME)[0];

		return new AuthenticationToken(userName, password);
	}

	@Override
	public boolean afterAuthenticate(Authentication authentication, HttpServletResponse response) {
		try {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

}
