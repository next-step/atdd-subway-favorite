package nextstep.auth.presentation;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }

        String token = authorization.split(" ")[1];
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String email = jwtTokenProvider.getPrincipal(token);
        return new LoginMember(email);
    }
}
