package nextstep.subway.auth.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.exception.NotFoundUserException;
import nextstep.subway.auth.exception.NotValidPasswordException;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

	private final AuthenticationConverter converter;
	private final CustomUserDetailsService userDetailsService;

	protected AuthenticationInterceptor(
		AuthenticationConverter converter, CustomUserDetailsService userDetailsService) {
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
		final LoginMember loginMember = userDetailsService.loadUserByUsername(principal);

		validAuthentication(authenticationToken, loginMember);

		return new Authentication(loginMember);
	}

	private void validAuthentication(AuthenticationToken authenticationToken, LoginMember userDetails) {
		if (userDetails == null) {
			throw new NotFoundUserException();
		}

		if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
			throw new NotValidPasswordException();
		}
	}
}
