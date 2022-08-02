package nextstep.auth.interceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.user.UserDetails;
import org.apache.catalina.User;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationChainHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {

            String authCredentials = extractCredentials(request);
            validAuthCredentials(authCredentials);

            UserDetails userDetails = getUserDetails(authCredentials);
            validUserDetails(userDetails);

            Authentication authentication = createAuthentication(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract String extractCredentials(HttpServletRequest request);

    protected abstract UserDetails getUserDetails(String authCredentials);

    protected abstract Authentication createAuthentication(UserDetails userDetails);

    private void validAuthCredentials(String authCredentials){
        if(ObjectUtils.isEmpty(authCredentials)){
            throw new AuthenticationException();
        }
    }

    private void validUserDetails(UserDetails userDetails){
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (userDetails.isValidPassword(userDetails.getPassword())) {
            throw new AuthenticationException();
        }
    }
}
