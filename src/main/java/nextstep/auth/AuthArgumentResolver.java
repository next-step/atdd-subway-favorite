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

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

	private final String BEARER_TOKEN = "Bearer ";
	private final String TOKEN = "token ";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean isAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
		return isAuthAnnotation;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		String headerAuth = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

		Class<?> isParameter = parameter.getParameterType();

		if (AuthMember.class.equals(isParameter)) {
			return checkHeaderMemberAuth(headerAuth);
		}

		if (AuthGithub.class.equals(isParameter)) {
			return checkHeaderGithubAuth(headerAuth);
		}

		throw new IllegalArgumentException(NOT_MATCH_REQUEST_PARAMETER.isMessage());
	}

	private AuthMember checkHeaderMemberAuth(String headerAuth) {
		checkHeaderNull(headerAuth, BEARER_TOKEN);
		checkHeaderContainToken(headerAuth, BEARER_TOKEN);

		String accessToken = getTokenFromHeader(headerAuth, BEARER_TOKEN);

		if (!jwtTokenProvider.validateToken(accessToken)) {
			throw new IllegalArgumentException(HEADER_AUTH_INVALID_TOKEN.isMessage());
		}

		String email = jwtTokenProvider.getPrincipal(accessToken);
		List<String> roles = jwtTokenProvider.getRoles(accessToken);
		return new AuthMember(email, roles);
	}

	private AuthGithub checkHeaderGithubAuth(String headerAuth) {
		checkHeaderNull(headerAuth, TOKEN);
		checkHeaderContainToken(headerAuth, TOKEN);

		String accessToken = getTokenFromHeader(headerAuth, TOKEN);
		return new AuthGithub(accessToken);
	}

	private void checkHeaderNull(String headerAuth, String type) {
		if (headerAuth == null || headerAuth.length() == type.length() - 1) {
			throw new IllegalArgumentException(HEADER_AUTH_IS_NULL.isMessage());
		}
	}

	private void checkHeaderContainToken(String headerAuth, String type) {
		String containBearer = headerAuth.substring(0, type.length());
		if (!containBearer.equals(type)) {
			throw new IllegalArgumentException(HEADER_AUTH_NOT_TOKEN.isMessage());
		}
	}

	private String getTokenFromHeader(String headerAuth, String type) {
		return headerAuth.replace(type, "");
	}
}
