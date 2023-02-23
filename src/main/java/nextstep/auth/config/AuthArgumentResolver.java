package nextstep.auth.config;

import nextstep.auth.application.JwtTokenProvider;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isRegUserAnnotation = parameter.getParameterAnnotation(AuthRequest.class) != null;

        return isRegUserAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object accessToken = webRequest.getAttribute("accessToken", 0);

        try {
            String email = jwtTokenProvider.getPrincipal(String.valueOf(accessToken));
            return email;
        } catch (Exception e) {
            throw new AuthenticationException("인증 요청값 accessToken이 문자열인지 확인 필요");
        }
    }
}
