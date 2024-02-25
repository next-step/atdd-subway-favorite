package subway.member;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
	private final JwtTokenProvider jwtTokenProvider;

	public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) throws AuthenticationException {
		String authorization = webRequest.getHeader("Authorization");

		checkAuthenticationIsNull(authorization);

		String[] split = authorization.split(" ");

		checkAuthorizationToken(split);

		String token = split[1];

		checkInvalidToken(token);

		String email = jwtTokenProvider.getPrincipal(token);

		return new LoginMember(email);
	}

	private void checkAuthenticationIsNull(String authorization) {
		if (Objects.isNull(authorization) || authorization.isEmpty()) {
			throw new AuthenticationException("로그인이 필요합니다.");
		}
	}

	private void checkAuthorizationToken(String[] splitAuthorization) {
		if (
			splitAuthorization.length < 2
				|| !"bearer".equalsIgnoreCase(splitAuthorization[0])
				|| splitAuthorization[1].isEmpty()
		) {
			throw new AuthenticationException("로그인이 필요합니다.");
		}
	}

	private void checkInvalidToken(String token) {
		if (!jwtTokenProvider.validateToken(token)) {
			throw new AuthenticationException("로그인이 필요합니다.");
		}
	}
}
