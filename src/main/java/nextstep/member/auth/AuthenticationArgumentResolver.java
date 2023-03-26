package nextstep.member.auth;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.AuthUser;
import nextstep.member.exception.InvalidTokenException;
import nextstep.member.exception.MemberErrorMessage;
import nextstep.member.exception.UnAuthenticationException;
import nextstep.member.ui.annotations.AuthToken;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
	private static final String AUTHORIZATION = "AUTHORIZATION";
	private static final String AUTHENTICATION_TYPE = "Bearer";

	private final JwtTokenProvider jwtTokenProvider;

	public AuthenticationArgumentResolver(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		final boolean isAuthTokenAnnotation = Objects.nonNull(parameter.getParameterAnnotation(AuthToken.class));
		final boolean isTokenRequest = Objects.equals(parameter.getParameterType(), AuthUser.class);
		return isAuthTokenAnnotation && isTokenRequest;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		String authorization = webRequest.getHeader(AUTHORIZATION);

		validateAuthorizationNonNull(authorization);
		validateTokenType(authorization);

		String token = getToken(authorization);

		validateToken(token);

		return getAuthUser(token);
	}

	private AuthUser getAuthUser(String token) {
		return AuthUser.of(jwtTokenProvider.getPrincipal(token), jwtTokenProvider.getRoles(token));
	}

	private void validateAuthorizationNonNull(String authorization) {
		if (!Objects.nonNull(authorization)) {
			throw new UnAuthenticationException(MemberErrorMessage.UNAUTHENTICATED_TOKEN);
		}
	}

	private void validateTokenType(String authorization) {
		if (!authorization.startsWith(AUTHENTICATION_TYPE)) {
			throw new UnAuthenticationException(MemberErrorMessage.UNAUTHENTICATED_TOKEN);
		}
	}

	private void validateToken(String token) {
		if (!jwtTokenProvider.validateToken(token)) {
			throw new InvalidTokenException(MemberErrorMessage.INVALID_TOKEN);
		}
	}

	private String getToken(String authorization) {
		return authorization.replace(String.format("%s ", AUTHENTICATION_TYPE), "");
	}

}
