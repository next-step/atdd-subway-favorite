package nextstep.config;

import nextstep.Exception.NoAuthorizationHeaderException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.UserEmail;
import nextstep.member.ui.UserInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthorizationResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("authorization");
        validateIncludeAuthorization(authorization);
        String[] tokenInfo = authorization.split(" ");
        String token = tokenInfo[1];
        String principal = jwtTokenProvider.getPrincipal(token);
        return new UserEmail(principal);
    }

    private void validateIncludeAuthorization(String authorization) {
        if (authorization==null) {
            throw new NoAuthorizationHeaderException();
        }
    }
}
