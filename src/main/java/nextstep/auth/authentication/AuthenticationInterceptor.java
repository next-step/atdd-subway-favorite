package nextstep.auth.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

	private final AuthenticationConverter converter;
	private final UserDetailsService userDetailsService;

	public AuthenticationInterceptor(AuthenticationConverter converter, UserDetailsService userDetailsService) {
		this.converter = converter;
		this.userDetailsService = userDetailsService;
	}

	public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		AuthenticationToken authenticationToken = converter.convert(request);
		Authentication authentication = authenticate(authenticationToken);

		afterAuthentication(request, response, authentication);
		return false;
	}

	private Authentication authenticate(AuthenticationToken token) {
		String principal = token.getPrincipal();
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, token);

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
}
