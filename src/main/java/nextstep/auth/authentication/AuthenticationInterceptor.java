package nextstep.auth.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
	private UserDetailsService userDetailsService;
	private AuthenticationConverter authenticationConverter;

	public AuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
		this.userDetailsService = userDetailsService;
		this.authenticationConverter = authenticationConverter;
	}

	public Authentication authenticate(AuthenticationToken authenticationToken) {
		String principal = authenticationToken.getPrincipal();
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, authenticationToken);

		return new Authentication(userDetails);
	}

	private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
		if (userDetails == null) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		IOException {
		AuthenticationToken token = authenticationConverter.convert(request);
		Authentication authentication = authenticate(token);
		afterAuthentication(request, response, authentication);

		return false;
	}

	public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
