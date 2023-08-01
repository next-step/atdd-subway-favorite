package nextstep.auth.principal;

import nextstep.auth.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int TOKEN_KEY = 0;
    private static final int TOKEN_INDEX = 1;
    private static final String TOKEN_DELIMITER = " ";
    private static final int TOKEN_SIZE = 2;
    private static final String BEARER_KEY = "bearer";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new AuthenticationException();
        }
        String[] tokenInfo = authorization.split(TOKEN_DELIMITER);
        if (tokenInfo.length != TOKEN_SIZE || !isBearerToken(tokenInfo)) {
            throw new AuthenticationException();
        }
        String token = tokenInfo[TOKEN_INDEX];

        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);

        return new UserPrincipal(username, role);
    }

    private static boolean isBearerToken(String[] tokenInfo) {
        return BEARER_KEY.equalsIgnoreCase(tokenInfo[TOKEN_KEY]);
    }
}
