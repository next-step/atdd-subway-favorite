package nextstep.subway.auth.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetail;
import nextstep.subway.auth.exception.NotFoundUserException;
import nextstep.subway.auth.exception.NotValidPasswordException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

	private final AuthenticationConverter converter;
	private final UserDetailsService userDetailsService;

	protected AuthenticationInterceptor(
		AuthenticationConverter converter, UserDetailsService userDetailsService) {
		this.converter = converter;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public boolean preHandle(
		HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		AuthenticationToken authenticationToken = converter.convert(request);
		Authentication authentication = authenticate(authenticationToken);

		afterAuthentication(request, response, authentication);

		return false;
	}

	protected abstract void afterAuthentication(
		HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException;

	private Authentication authenticate(AuthenticationToken authenticationToken) {
		final String principal = authenticationToken.getPrincipal();
		final UserDetail userDetail = userDetailsService.loadUserByUsername(principal);

		validAuthentication(authenticationToken, userDetail);

		return new Authentication(userDetail);
	}

	private void validAuthentication(AuthenticationToken authenticationToken, UserDetail userDetails) {
		if (userDetails == null) {
			throw new NotFoundUserException();
		}

		if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
			throw new NotValidPasswordException();
		}
	}
}
