package nextstep.auth.principal;

import lombok.RequiredArgsConstructor;
import nextstep.global.error.code.ErrorCode;
import nextstep.global.error.exception.AuthenticationException;
import nextstep.auth.token.service.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    private final String BEARER_TOKEN_KEY = "bearer";

    private final String DELIMITER = " ";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException(ErrorCode.AUTHORIZATION_HEADER_IS_BLANK);
        }

        if (!BEARER_TOKEN_KEY.equalsIgnoreCase(authorization.split(DELIMITER)[0])) {
            throw new AuthenticationException(ErrorCode.INVALID_BEARER_GRANT_TYPE);
        }
        String token = authorization.split(DELIMITER)[1];

        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);

        return UserPrincipal.builder()
                .username(username)
                .role(role)
                .build();
    }
}
