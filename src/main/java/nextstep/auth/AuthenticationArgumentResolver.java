package nextstep.auth;

import io.jsonwebtoken.MalformedJwtException;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.security.sasl.AuthenticationException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String AUTHENTICATION_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Objects.nonNull(parameter.getParameterAnnotation(Authentication.class)) && Objects.equals(parameter.getParameterType(), String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(AUTHORIZATION);

        if (Objects.nonNull(authorization) && !authorization.startsWith(AUTHENTICATION_TYPE)) {
            throw new AuthenticationException("UnAuthorized token type.");
        }

        String token = authorization.replace(String.format("%s ", AUTHENTICATION_TYPE), "");

        if (!jwtTokenProvider.validateToken(token)) {
            throw new MalformedJwtException(String.format("%s is UnAuthorized token", token));
        }

        return jwtTokenProvider.getPrincipal(token);
    }
}
