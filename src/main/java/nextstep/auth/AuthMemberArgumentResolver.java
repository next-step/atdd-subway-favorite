package nextstep.auth;

import static nextstep.common.ErrorMsg.*;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.AuthorizationException;

@Component
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final String BEARER_TOKEN = "Bearer ";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean isAuthAnnotation =  parameter.getParameterAnnotation(Auth.class) != null;
		boolean isAuthMemberClass = AuthMember.class.equals(parameter.getParameterType());
		return isAuthAnnotation && isAuthMemberClass;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		String headerAuth = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

		String accessToken = checkHeaderAuth(headerAuth);

		Long id = jwtTokenProvider.getPrincipal(accessToken);
		String email = jwtTokenProvider.getEmail(accessToken);
		List<String> roles = jwtTokenProvider.getRoles(accessToken);
		return new AuthMember(id, email, roles);
	}

	private String checkHeaderAuth(String headerAuth) {
		if (headerAuth == null || headerAuth.length() == BEARER_TOKEN.length() - 1) {
			throw new AuthorizationException(HEADER_AUTH_IS_NULL.isMessage());
		}

		String containBearer = headerAuth.substring(0, BEARER_TOKEN.length());
		if (!containBearer.equals(BEARER_TOKEN)) {
			throw new AuthorizationException(HEADER_AUTH_NOT_BEARER.isMessage());
		}

		String accessToken = headerAuth.replace(BEARER_TOKEN, "");
		if (!jwtTokenProvider.validateToken(accessToken)) {
			throw new AuthorizationException(HEADER_AUTH_INVALID_TOKEN.isMessage());
		}
		return accessToken;
	}
}
