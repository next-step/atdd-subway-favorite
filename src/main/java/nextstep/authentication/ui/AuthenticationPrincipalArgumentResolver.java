package nextstep.authentication.ui;

import io.jsonwebtoken.JwtException;
import nextstep.authentication.application.exception.AuthenticationException;
import nextstep.authentication.application.JwtTokenProvider;
import nextstep.authentication.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
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
        String token = getToken(webRequest);

        try {
            String email = jwtTokenProvider.getPrincipal(token);
            Long id = jwtTokenProvider.getId(token);
            return new LoginMember(email, id);
        } catch (JwtException e) {
            throw new AuthenticationException();
        }
    }

    private String getToken(NativeWebRequest webRequest) {
        String authorization = webRequest.getHeader("Authorization");
        if (isUnauthorized(authorization)) {
            throw new AuthenticationException();
        }

        return authorization.split(" ")[1];
    }

    private boolean isUnauthorized(String authorization) {
        return authorization == null || !"bearer".equalsIgnoreCase(authorization.split(" ")[0]);
    }
}
