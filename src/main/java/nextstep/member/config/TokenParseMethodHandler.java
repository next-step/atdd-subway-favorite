package nextstep.member.config;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.AuthToken;
import nextstep.member.domain.TokenInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class TokenParseMethodHandler implements HandlerMethodArgumentResolver {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private final JwtTokenProvider jwtTokenProvider;

	public TokenParseMethodHandler(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthToken.class) && isEqualEmailTypeAndParameterType(parameter);
	}

	@Override
	public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest httpServletRequest = (HttpServletRequest)webRequest.getNativeRequest();

		String accessToken = BearerTokenExtractor.extract(httpServletRequest.getHeader(AUTHORIZATION_HEADER));

		return jwtTokenProvider.getPrincipal(accessToken);
	}

	private boolean isEqualEmailTypeAndParameterType(MethodParameter methodParameter) {
		AuthToken authToken = methodParameter.getParameterAnnotation(AuthToken.class);
		boolean isEqualEmailType = authToken.value() == TokenInfo.EMAIL;
		boolean isEqualParameterType = methodParameter.getParameterType() == String.class;

		return isEqualEmailType && isEqualParameterType;
	}
}
