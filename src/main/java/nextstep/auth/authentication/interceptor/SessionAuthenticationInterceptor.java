package nextstep.auth.authentication.interceptor;

import static nextstep.auth.context.SecurityContextHolder.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {


	public SessionAuthenticationInterceptor(SessionAuthenticationConverter sessionAuthenticationConverter,
		UserDetailsService userDetailsService) {
		super(sessionAuthenticationConverter, userDetailsService);
	}

	@Override
	public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
