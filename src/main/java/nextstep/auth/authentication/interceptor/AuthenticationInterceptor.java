package nextstep.auth.authentication.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.member.domain.UserDetails;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {


	private final AuthenticationConverter authenticationConverter;
	private final UserDetailsService userDetailsService;

	AuthenticationInterceptor(AuthenticationConverter authenticationConverter, UserDetailsService userDetailsService) {
		this.authenticationConverter = authenticationConverter;
		this.userDetailsService = userDetailsService;
	}

	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		return authenticationConverter.convert(request);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws IOException {
		AuthenticationToken token = convert(request);
		Authentication authentication = authenticate(token);
		afterAuthentication(request, response, authentication);
		return false;
	}

	public Authentication authenticate(AuthenticationToken token) {
		String principal = token.getPrincipal();
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, token);
		return new Authentication(userDetails);
	}

	public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException;

	private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
		if (userDetails == null) {
			throw new AuthenticationException();
		}
		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}
}
