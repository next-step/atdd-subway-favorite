package subway.auth.principal;

import lombok.RequiredArgsConstructor;
import subway.constant.SubwayMessage;
import subway.exception.AuthenticationException;
import subway.auth.token.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader("Authorization");
        validAuthorization(authorization);
        String token = authorization.split(" ")[1];
        validToken(token);
        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);

        return new UserPrincipal(username, role);
    }

    private void validToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException(SubwayMessage.AUTH_INVALID_TOKEN);
        }
    }

    private static void validAuthorization(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException(SubwayMessage.AUTH_TOKEN_NOT_FOUND_FROM_HEADERS);
        }
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException(SubwayMessage.AUTH_TOKEN_NOT_FOUND_FROM_HEADERS);
        }
    }
}
