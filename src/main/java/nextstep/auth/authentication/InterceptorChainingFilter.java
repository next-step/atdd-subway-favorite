package nextstep.auth.authentication;

import nextstep.auth.authentication.exception.AuthenticationException;
import nextstep.auth.authentication.exception.BearerAuthenticationException;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class InterceptorChainingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isAlreadyLogin()) {
            return true;
        }

        try {
            UserDetails userDetails = getUserDetails(request);
            setAuthentication(userDetails);
        } catch (BearerAuthenticationException e) {
//            throw new BearerAuthenticationException();
        } catch (AuthenticationException | IndexOutOfBoundsException ignore) {
            // BasicAuthenticationFilter에서만 발생. Bearer 인증으로 들어온다.
        }

        return true;
    }

    private boolean isAlreadyLogin() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    protected abstract UserDetails getUserDetails(HttpServletRequest request);

    protected abstract void setAuthentication(UserDetails userDetails);
}
