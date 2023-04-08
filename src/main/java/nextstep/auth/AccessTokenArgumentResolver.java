package nextstep.auth;

import static nextstep.exception.ExceptionMsg.TOKEN_FORMAT_IS_NOT_VALID;

import javax.servlet.http.HttpServletResponse;
import nextstep.exception.ApiException;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider tokenProvider;

    private static final String TYPE_START = "Bearer ";

    public AccessTokenArgumentResolver(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AccessToken.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory) {
        String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasLength(authorizationHeader) || !authorizationHeader.startsWith(TYPE_START)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, TOKEN_FORMAT_IS_NOT_VALID);
        }
        String token = authorizationHeader.substring(TYPE_START.length());

        if (!tokenProvider.validateToken(token)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, TOKEN_FORMAT_IS_NOT_VALID);
        }

        return new AuthorizedUser(tokenProvider.getPrincipal(token),
            tokenProvider.getRoles(token));
    }

}
