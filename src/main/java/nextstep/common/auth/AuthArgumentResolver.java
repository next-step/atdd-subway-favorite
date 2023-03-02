package nextstep.common.auth;

import nextstep.login.infra.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ACCESS_TOKEN_OAUTH_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthArgumentResolver(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(VerifiedMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String authHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            return null;
        }

        if (isOAuthToken(authHeader)) {
            return jwtTokenProvider.decodeToken(authHeader.substring(ACCESS_TOKEN_OAUTH_TYPE.length()));
        }

        return null;
    }

    private boolean isOAuthToken(String authHeader) {
        return authHeader.startsWith(ACCESS_TOKEN_OAUTH_TYPE);
    }
}
