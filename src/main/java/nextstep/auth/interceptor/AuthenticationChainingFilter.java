package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationChainingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isAlreadyLoginUser()) {
            return true;
        }

        try {
            AuthenticationToken token = convert(request);
            UserDetails userDetails = findUserDetails(token);
            afterAuthentication(userDetails);
            return true;
        } catch (AuthenticationException e) {
            return true;
        }
    }

    private boolean isAlreadyLoginUser() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private void afterAuthentication(UserDetails userDetails) {
        Authentication authentication = new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request);
    protected abstract UserDetails findUserDetails(AuthenticationToken token);
}
