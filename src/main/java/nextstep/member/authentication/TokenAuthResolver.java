package nextstep.member.authentication;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.LoginMemberRequest;
import nextstep.member.exception.InvalidTokenException;

@Component
public class TokenAuthResolver implements HandlerMethodArgumentResolver {
    private static final String BEARER_TOKEN = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = getAuthorization(webRequest);

        if (!jwtTokenProvider.validateToken(authorization)) {
            throw new InvalidTokenException();
        }

        String principal = jwtTokenProvider.getPrincipal(authorization);
        List<String> roles = jwtTokenProvider.getRoles(authorization);

        return new LoginMemberRequest(principal, roles);
    }

    private String getAuthorization(NativeWebRequest webRequest) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(BEARER_TOKEN)) {
            throw new InvalidTokenException();
        }

        return authorization.replace(BEARER_TOKEN, "");
    }
}
