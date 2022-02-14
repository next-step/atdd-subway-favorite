package nextstep.auth.authorization;

import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCheckInterceptor extends SecurityContextInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public LoginCheckInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (credentials.isEmpty()) {
            return false;
        }

        if (!jwtTokenProvider.validateToken(credentials)) {
            return false;
        }

        return super.preHandle(request, response, handler);
    }
}
