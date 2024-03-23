package nextstep.auth.ui;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.favorite.domain.LoginMemberForFavorite;
import nextstep.favorite.ui.LoginMemberForFavoritePrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberForFavoritePrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberForFavoritePrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberForFavoritePrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader("Authorization");
        if (authorization == null || isNotBearerTokenHeader(authorization)) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        String email = jwtTokenProvider.getPrincipal(token);

        return new LoginMemberForFavorite(email);
    }

    private static boolean isNotBearerTokenHeader(String authorization) {
        return !"bearer".equalsIgnoreCase(authorization.split(" ")[0]);
    }
}
