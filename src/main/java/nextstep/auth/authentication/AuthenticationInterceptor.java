package nextstep.auth.authentication;

import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.domain.AuthenticatedMember;
import nextstep.auth.service.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.authentication.AuthenticationException.NOT_FOUND_EMAIL;
import static nextstep.auth.authentication.AuthenticationException.PASSWORD_IS_INCORRECT;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

	private final UserDetailsService userDetailsService;
	private final AuthenticationConverter authenticationConverter;

	public AuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
		this.userDetailsService = userDetailsService;
		this.authenticationConverter = authenticationConverter;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		AuthenticationToken token = authenticationConverter.convert(request);
		Authentication authentication = authenticate(token);
		afterAuthentication(request, response, authentication);
		return false;
	}

	public abstract void afterAuthentication(HttpServletRequest request,
											 HttpServletResponse response,
											 Authentication authentication) throws IOException;

	protected Authentication authenticate(AuthenticationToken token) {
		String principal = token.getPrincipal();
		AuthenticatedMember userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, token);

		return new Authentication(userDetails);
	}

	private void checkAuthentication(AuthenticatedMember userDetails, AuthenticationToken token) {
		if (userDetails == null) {
			throw new AuthenticationException(NOT_FOUND_EMAIL);
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException(PASSWORD_IS_INCORRECT);
		}
	}

}
