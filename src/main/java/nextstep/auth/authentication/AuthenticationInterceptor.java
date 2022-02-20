package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
	protected AuthenticationConverter authenticationConverter;
	private final CustomUserDetailsService userDetailsService;

	protected AuthenticationInterceptor(CustomUserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
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

	public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
