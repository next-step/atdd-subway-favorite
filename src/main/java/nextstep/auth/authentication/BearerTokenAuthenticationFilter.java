package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ObjectUtils;

import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

public class BearerTokenAuthenticationFilter extends AuthenticationChainInterceptor {
	private JwtTokenProvider jwtTokenProvider;

	public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String accessToken = convert(request);
			Authentication authentication = authenticate(accessToken);
			super.afterAuthenticate(authentication);
			return true;
		} catch (Exception e) {
			return true;
		}
	}

	private String convert(HttpServletRequest request) {
		String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

		if (ObjectUtils.isEmpty(accessToken)) {
			throw new AuthenticationException();
		}
		if (!jwtTokenProvider.validateToken(accessToken)) {
			throw new AuthenticationException();
		}
		return accessToken;
	}

	private Authentication authenticate(String accessToken) {
		String principal = jwtTokenProvider.getPrincipal(accessToken);
		return new Authentication(principal, jwtTokenProvider.getRoles(accessToken));
	}
}
