package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.AuthenticationProvider;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

    protected final AuthenticationProvider authenticationProvider;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;

    public AbstractAuthenticationInterceptor(AuthenticationProvider authenticationProvider, AuthenticationTokenExtractor authenticationTokenExtractor) {

        this.authenticationProvider = authenticationProvider;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = authenticationTokenExtractor.extract(request);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);

        applyAuthentication(authenticate, request, response);

        response.setStatus(HttpServletResponse.SC_OK);
        return false;
    }

    protected abstract void applyAuthentication(Authentication authenticate, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
