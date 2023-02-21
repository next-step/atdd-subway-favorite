package nextstep.member.auth;

import nextstep.member.domain.LoginMember;
import nextstep.member.exception.TokenAuthorizationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public LoginArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            throw new TokenAuthorizationException();
        }

        if (authorization.startsWith(BEARER)) {
            authorization = authorization.replace(BEARER, "");
        }

        if (!jwtTokenProvider.validateToken(authorization)) {
            throw new TokenAuthorizationException();
        }

        String principal = jwtTokenProvider.getPrincipal(authorization);
        List<String> roles = jwtTokenProvider.getRoles(authorization);

        return new LoginMember(principal, roles);

    }


}
