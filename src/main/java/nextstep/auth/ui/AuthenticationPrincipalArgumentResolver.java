package nextstep.auth.ui;

import nextstep.exception.AuthenticationException;
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
        if (authorization == null || !"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException("잘못된 형식의 토큰입니다.");
        }
        String token = splitAuthorization(authorization);

        if(jwtTokenProvider.validExpiredToken(token)) {
            throw new AuthenticationException("토큰이 만료되었습니다.");
        }

        String email = jwtTokenProvider.getPrincipal(token);

        return new LoginMember(email);
    }

    private String splitAuthorization(String authorization) {
        if(authorization.split(" ").length < 2) {
            throw new AuthenticationException("잘못된 형식의 토큰입니다.");
        }

        return authorization.split(" ")[1];
    }
}
