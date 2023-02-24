package nextstep.config;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.LoginUser;
import nextstep.member.domain.exception.IllegalAccessTokenException;
import nextstep.member.ui.LoginedUser;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthorizationResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_PREFIX = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        String accessToken = getAccessToken(authorization);

        checkAccessToken(accessToken);
        String principal = jwtTokenProvider.getPrincipal(accessToken);
        return new LoginUser(Long.parseLong(principal));
    }

    private String getAccessToken(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new IllegalAccessTokenException();
        }

        String[] splitAccessToken = authorization.split(" ");

        if (splitAccessToken.length != 2) {
            throw new IllegalAccessTokenException();
        }

        if (!splitAccessToken[0].equals(TOKEN_PREFIX)) {
            throw new IllegalAccessTokenException();
        }

        String accessToken = splitAccessToken[1];

        if (accessToken.isBlank()) {
            throw new IllegalAccessTokenException();
        }

        return accessToken;
    }

    private void checkAccessToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalAccessTokenException();
        }
    }

}
