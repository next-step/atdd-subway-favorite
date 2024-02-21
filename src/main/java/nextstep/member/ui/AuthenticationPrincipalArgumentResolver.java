package nextstep.member.ui;

import nextstep.member.AuthenticationException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
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
        String authorization;
        String token;
        String email;

        authorization = webRequest.getHeader("Authorization");

        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }

        try {
            token = authorization.split(" ")[1];
            email = jwtTokenProvider.getPrincipal(token);
        }catch (Exception e) {
            throw new AuthenticationException();
        }

        return LoginMember.from(email);
    }
}
