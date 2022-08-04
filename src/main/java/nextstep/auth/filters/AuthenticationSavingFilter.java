package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationSavingFilter<T> implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            T token = convert(request);
            UserDetails userDetails = validate(token);
            authenticate(userDetails);
            return true;
        } catch (AuthenticationException e) {
            return true;
        }
    }

    protected abstract T convert(HttpServletRequest request);

    protected abstract UserDetails validate(T token);

    protected void authenticate(UserDetails userDetails) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
