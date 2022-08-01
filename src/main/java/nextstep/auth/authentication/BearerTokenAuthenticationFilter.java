package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
	private JwtTokenProvider jwtTokenProvider;

	public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

			if (ObjectUtils.isEmpty(accessToken)) {
				throw new AuthenticationException();
			}
			if (!jwtTokenProvider.validateToken(accessToken)) {
				throw new AuthenticationException();
			}
			String principal = jwtTokenProvider.getPrincipal(accessToken);
			Authentication authentication = new Authentication(principal, jwtTokenProvider.getRoles(accessToken));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return true;
		} catch (Exception e) {
			return true;
		}
	}
}
