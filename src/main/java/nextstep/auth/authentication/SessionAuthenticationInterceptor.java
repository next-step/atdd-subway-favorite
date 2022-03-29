package nextstep.auth.authentication;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {

	public SessionAuthenticationInterceptor(AuthenticationConverter converter, UserDetailsService userDetailsService) {
		super(converter, userDetailsService);
	}

	@Override
	public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
