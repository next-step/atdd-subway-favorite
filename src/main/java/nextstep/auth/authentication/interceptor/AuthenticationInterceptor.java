package nextstep.auth.authentication.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
	private AuthenticationConverter authenticationConverter;

	AuthenticationInterceptor(AuthenticationConverter authenticationConverter) {
		this.authenticationConverter = authenticationConverter;
	}

	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		return authenticationConverter.convert(request);
	}

	public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException;

}
