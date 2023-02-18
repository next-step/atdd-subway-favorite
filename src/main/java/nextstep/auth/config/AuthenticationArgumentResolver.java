package nextstep.auth.config;

import nextstep.auth.application.AuthService;
import nextstep.auth.domain.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private AuthService authService;

    public AuthenticationArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        return authService.findMemberByToken(credentials);
    }
}