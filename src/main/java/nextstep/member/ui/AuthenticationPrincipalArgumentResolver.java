package nextstep.member.ui;

import nextstep.member.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.security.application.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider<LoginMember> jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(final JwtTokenProvider<LoginMember> jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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
        String[] authorizationSplit = authorization.split(" ");
        if (authorizationSplit.length != 2) {
            throw new AuthenticationException();
        }
        if (!"bearer".equalsIgnoreCase(authorizationSplit[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        return jwtTokenProvider.parseToken(token);
    }
}
