package nextstep.member.auth;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.auth.exception.AuthorizationException;
import nextstep.member.ui.dto.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginUser.class);
    }

    @Override
    public LoginUser resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER)) {
            throw new AuthorizationException();
        }

        final String accessToken = getAccessToken(authorization);

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException();
        }

        final String principal = jwtTokenProvider.getPrincipal(accessToken);
        return new LoginUser(principal);
    }

    private String getAccessToken(String authorization) {
        return authorization.replace(BEARER, "");
    }
}
