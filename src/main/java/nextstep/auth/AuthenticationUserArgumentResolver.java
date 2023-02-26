package nextstep.auth;

import nextstep.member.application.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.security.sasl.AuthenticationException;

import static nextstep.common.constants.ErrorConstant.INVALID_AUTHENTICATION_INFO;

@Component
public class AuthenticationUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String SCHEME_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationUserArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticationUser.class) != null
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {

        final String authorization = webRequest.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(authorization) && !authorization.startsWith(SCHEME_TYPE)) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_INFO);
        }

        final String token = authorization.substring(SCHEME_TYPE.length() + 1);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_INFO);
        }

        return jwtTokenProvider.getPrincipal(token);
    }
}
