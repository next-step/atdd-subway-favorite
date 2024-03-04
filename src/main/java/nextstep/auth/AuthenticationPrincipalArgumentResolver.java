package nextstep.auth;

import nextstep.exception.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
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

        if(!StringUtils.hasText(authorization)) {
            throw new AuthenticationException("인증정보가 존재하지 않습니다.");
        }

        if(authorization.trim().equalsIgnoreCase("bearer")) {
            throw new AuthenticationException("인증정보가 존재하지 않습니다.");
        }

        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        return new LoginMember(jwtTokenProvider.getId(token), jwtTokenProvider.getPrincipal(token));
    }
}
