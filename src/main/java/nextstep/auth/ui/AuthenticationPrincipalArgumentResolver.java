package nextstep.auth.ui;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.domain.UserDetailsService;
import nextstep.auth.exception.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");

        if (authorization == null) {
            throw new AuthenticationException();
        }

        String[] authHeader = authorization.split(" ");

        if (!"bearer".equalsIgnoreCase(authHeader[0]) || authHeader.length != 2) {
            throw new AuthenticationException();
        }

        String token = authHeader[1];

        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getPrincipal(token);
            return userDetailsService.loadByUserEmail(email);
        }

        throw new AuthenticationException();
    }
}
