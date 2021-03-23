package nextstep.subway.auth.ui.session;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.AuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;

public class SessionAuthInterceptor extends AuthenticationInterceptor {

	public SessionAuthInterceptor(SessionAuthenticationConverter converter,
		CustomUserDetailsService userDetailsService) {
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
