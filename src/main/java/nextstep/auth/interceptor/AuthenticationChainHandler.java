package nextstep.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public abstract class AuthenticationChainHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        try {
            String authCredentials = extractCredentials(request);
            validAuthCredentials(authCredentials);

            UserDetails userDetails = getUserDetails(authCredentials);
            validUserDetails(userDetails);

            Authentication authentication = createAuthentication(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) { }
        return true;
    }

    public abstract String extractCredentials(HttpServletRequest request);

    public abstract UserDetails getUserDetails(String authCredentials);

    public abstract Authentication createAuthentication(UserDetails userDetails);

    private void validAuthCredentials(String authCredentials) {
        if(ObjectUtils.isEmpty(authCredentials)) {
            throw new AuthenticationException();
        }
    }

    private void validUserDetails(UserDetails userDetails) {
        isNullUserDetails(userDetails);
    }

    private void isNullUserDetails(UserDetails userDetails) {
        if(userDetails == null) {
            throw new AuthenticationException();
        }
    }

}
