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
        String authorization = webRequest.getHeader("Authorization");
        checkTokenExists(authorization);

        final String token = authorization.split(" ")[1];
        checkValidToken(token);

        final String email = jwtTokenProvider.getPrincipal(token);
        return new LoginMember(email);
    }

    private void checkTokenExists(String authorization) {
        if (authorization == null || !"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException("인증 토큰이 없습니다.");
        }
    }

    private void checkValidToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("유효한 인증 토큰이 아닙니다.");
        }
    }
}
