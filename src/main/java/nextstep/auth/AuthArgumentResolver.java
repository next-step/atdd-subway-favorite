package nextstep.auth;

import nextstep.error.exception.AuthException;
import nextstep.error.exception.BusinessException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static nextstep.error.exception.ErrorCode.INVALID_TOKEN;
import static nextstep.error.exception.ErrorCode.MISMATCHED_AUTHKEY;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String BEARER_PREFIX = "Bearer";

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberService memberService;

	public AuthArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.memberService = memberService;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Auth.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorization == null || !BEARER_PREFIX.equals(authorization.split(" ")[0])) {
			throw new BusinessException(MISMATCHED_AUTHKEY);
		}

		String accessToken = authorization.split(" ")[1];

		if (!jwtTokenProvider.validateToken(accessToken)) {
			throw new AuthException(INVALID_TOKEN);
		}
		String email = jwtTokenProvider.getPrincipal(accessToken);

		AuthMember authMember = memberService.findAuthMemberByEmail(email);

		return authMember;
	}
}
