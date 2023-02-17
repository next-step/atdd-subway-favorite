package nextstep.auth;

import nextstep.exception.InvalidAccessTokenException;
import nextstep.exception.AuthorizationException;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new MissingRequestHeaderException(HttpHeaders.AUTHORIZATION, parameter);
        }

        if (!BEARER_TOKEN_PREFIX.equals(authorization.split(" ")[0])) {
            throw new AuthorizationException(authorization);
        }

        String accessToken = authorization.split(" ")[1];
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new InvalidAccessTokenException(accessToken);
        }

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(accessToken));
        List<String> roles = jwtTokenProvider.getRoles(accessToken);
        return new LoginMember(id, roles);
    }

}
