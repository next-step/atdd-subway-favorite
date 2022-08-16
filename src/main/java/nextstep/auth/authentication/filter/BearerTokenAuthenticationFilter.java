package nextstep.auth.authentication.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.authentication.token.BearerAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final AuthenticationManager authenticationManager;

    public BearerTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private Authentication attemptAuthentication(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        Authentication token = new BearerAuthenticationToken(authCredentials);

        return authenticationManager.authenticate(token);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (checkAlreadyAuthentication()) {
            return true;
        }

        try {
            Authentication authentication = attemptAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (AuthenticationException e) {
            throw new AuthenticationException();
        } catch (Exception e) {
            return true;
        }
    }

    private boolean checkAlreadyAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null;
    }
}
