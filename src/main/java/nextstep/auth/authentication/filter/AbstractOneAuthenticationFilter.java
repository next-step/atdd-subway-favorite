package nextstep.auth.authentication.filter;

import static nextstep.auth.authentication.execption.InvalidTokenException.INVALID_TOKEN;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.execption.InvalidTokenException;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AbstractOneAuthenticationFilter implements HandlerInterceptor {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public AbstractOneAuthenticationFilter(AuthenticationManager authenticationManager,
                                           AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationManager = authenticationManager;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        try {
            Authentication authentication = attemptAuthentication(request, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            return false;
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException(INVALID_TOKEN);
        } catch (Exception e) {
            return false;
        }
    }

    protected abstract Authentication convert(HttpServletRequest request,
                                              HttpServletResponse response) throws IOException;

    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication token = convert(request, response);

        return authenticationManager.authenticate(token);
    }
}
