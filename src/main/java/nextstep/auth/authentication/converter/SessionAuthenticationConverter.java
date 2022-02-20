package nextstep.auth.authentication.converter;

import static nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import nextstep.auth.authentication.AuthenticationToken;

@Component
public class SessionAuthenticationConverter implements AuthenticationConverter {
	private static final String USERNAME_FIELD = "username";
	private static final String PASSWORD_FIELD = "password";
	
	@Override
	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		String principal = request.getParameter(USERNAME_FIELD);
		String credentials = request.getParameter(PASSWORD_FIELD);
		return new AuthenticationToken(principal, credentials);
	}
}
