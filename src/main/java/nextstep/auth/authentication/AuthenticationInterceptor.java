package nextstep.auth.authentication;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.domain.UserDetail;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
	protected AuthenticationConverter authenticationConverter;
	private final UserDetailService userDetailsService;

	protected AuthenticationInterceptor(UserDetailService userDetailsService, AuthenticationConverter authenticationConverter) {
		this.userDetailsService = userDetailsService;
		this.authenticationConverter = authenticationConverter;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		AuthenticationToken token = convert(request);
		Authentication authentication = authenticate(token);

		afterAuthentication(request, response, authentication);

		return false;
	}

	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		return authenticationConverter.convert(request);
	}

	public Authentication authenticate(AuthenticationToken token) {
		String principal = token.getPrincipal();
		UserDetail userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, token);

		return new Authentication(userDetails);
	}

	private void checkAuthentication(UserDetail userDetails, AuthenticationToken token) {
		if (userDetails == null) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}

	public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
