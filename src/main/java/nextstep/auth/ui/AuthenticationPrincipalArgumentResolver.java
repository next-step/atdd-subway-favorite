package nextstep.auth.ui;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "bearer";
    private static final String SPACE = " ";

    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(AUTHORIZATION);
        if (authorization == null || authorization.isEmpty() || !BEARER.equalsIgnoreCase(authorization.split(SPACE)[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(SPACE)[1];

        String email = jwtTokenProvider.getPrincipal(token);

        return new LoginMember(email);
    }
}
