package nextstep.auth.authentication.interceptor;

import static nextstep.auth.context.SecurityContextHolder.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {
	public static final String USERNAME_FIELD = "username";
	public static final String PASSWORD_FIELD = "password";
	private final CustomUserDetailsService userDetailsService;

	public SessionAuthenticationInterceptor(SessionAuthenticationConverter sessionAuthenticationConverter,
		CustomUserDetailsService userDetailsService) {
		super(sessionAuthenticationConverter);
		this.userDetailsService = userDetailsService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws IOException {
		AuthenticationToken token = convert(request);
		Authentication authentication = authenticate(token);

		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
		response.setStatus(HttpServletResponse.SC_OK);
		return false;
	}

	// public AuthenticationToken convert(HttpServletRequest request) {
	//     Map<String, String[]> paramMap = request.getParameterMap();
	//     String principal = paramMap.get(USERNAME_FIELD)[0];
	//     String credentials = paramMap.get(PASSWORD_FIELD)[0];
	//
	//     return new AuthenticationToken(principal, credentials);
	// }

	public Authentication authenticate(AuthenticationToken token) {
		String principal = token.getPrincipal();
		LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, token);

		return new Authentication(userDetails);
	}

	private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
		if (userDetails == null) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}

	@Override
	public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

	}
}
