package nextstep.api.auth.interfaces.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.api.auth.domain.operators.JwtTokenProvider;
import nextstep.common.annotation.AuthenticationPrincipal;
import nextstep.common.exception.auth.AuthenticationException;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String token = parseToken(webRequest);

		if (!jwtTokenProvider.validateToken(token)) {
			throw new AuthenticationException("Invalid or expired JWT token");
		}

		return UserPrincipal.of(jwtTokenProvider.getPrincipal(token));
	}

	private String parseToken(NativeWebRequest webRequest) {
		String authorization = webRequest.getHeader("Authorization");

		if (isTokenFormatNotValid(authorization)) {
			throw new AuthenticationException();
		}

		return authorization.split(" ")[1];
	}

	private boolean isTokenFormatNotValid(String authorization) {
		return authorization == null || !"bearer".equalsIgnoreCase(authorization.split(" ")[0]);
	}
}
