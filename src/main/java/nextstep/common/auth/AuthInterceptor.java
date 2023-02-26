package nextstep.common.auth;

import nextstep.login.application.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String ACCESS_TOKEN_OAUTH_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        if (isNotHandlerMethod(handler) || isNotLoginRequired(handler)) {
            return true;
        }

        if (isNotExistAuthorization(request)) {
            throw new TokenNotExistException();
        }

        if (isOAuthToken(request)) {
            validateOAuthToken(request);
        }

        return true;
    }


    private boolean isNotHandlerMethod(Object handler) {
        return !(handler instanceof HandlerMethod);
    }

    private boolean isNotLoginRequired(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);

        return loginRequired == null || loginRequired.allowGuest();
    }

    private boolean isNotExistAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) == null;
    }

    private boolean isOAuthToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION).startsWith(ACCESS_TOKEN_OAUTH_TYPE);
    }

    private void validateOAuthToken(final HttpServletRequest request) {
        jwtTokenProvider.validateToken(
                request.getHeader(HttpHeaders.AUTHORIZATION).substring(ACCESS_TOKEN_OAUTH_TYPE.length())
        );
    }
}
