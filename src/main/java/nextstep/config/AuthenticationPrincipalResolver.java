package nextstep.config;

import nextstep.exception.member.AuthTokenIsExpiredException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.AuthenticationPrincipal;
import nextstep.utils.ObjectStringMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationPrincipalResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
                && parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (tokenIsExpired(accessToken)) {
            throw new AuthTokenIsExpiredException();
        }

        String principal = jwtTokenProvider.getPrincipal(accessToken);
        return ObjectStringMapper.convertStringToLoginMember(principal, LoginMember.class);
    }

    private boolean tokenIsExpired(String accessToken) {
        return !jwtTokenProvider.validateToken(accessToken);
    }
}
