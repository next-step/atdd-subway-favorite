package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            var token = getToken(request);

            if (!jwtTokenProvider.validateToken(token)) {
                throw new AuthenticationException();
            }

            var principal = jwtTokenProvider.getPrincipal(token);
            var roles = jwtTokenProvider.getRoles(token);


            var authentication = new Authentication(principal, roles);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private String getToken(HttpServletRequest request) {
        return request.getHeader("authorization").split(" ")[1];
    }
}
