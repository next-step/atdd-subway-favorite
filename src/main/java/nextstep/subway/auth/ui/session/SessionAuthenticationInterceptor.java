package nextstep.subway.auth.ui.session;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.AuthenticationInterceptor;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {

	public SessionAuthenticationInterceptor(SessionAuthenticationConverter converter,
		UserDetailsService userDetailsService) {
		super(converter, userDetailsService);
	}

	@Override
	protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
