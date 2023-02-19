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

    private final AuthService authService;

    public AuthenticationArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        return authService.findMemberByToken(credentials);
    }
}