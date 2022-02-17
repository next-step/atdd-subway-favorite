package nextstep.auth.authentication.converter;

import static nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import nextstep.auth.authentication.AuthenticationToken;

@Component
public class SessionAuthenticationConverter implements AuthenticationConverter {

	@Override
	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		Map<String, String[]> paramMap = request.getParameterMap();
		String principal = paramMap.get(USERNAME_FIELD)[0];
		String credentials = paramMap.get(PASSWORD_FIELD)[0];

		return new AuthenticationToken(principal, credentials);	}
}
