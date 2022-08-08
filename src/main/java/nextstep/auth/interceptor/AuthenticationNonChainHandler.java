package nextstep.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationNonChainHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        AuthenticationToken authenticationToken = getAuthenticationToken(request);

        UserDetails userDetails = getUserDetails(authenticationToken);
        validUserDetails(userDetails, authenticationToken.getCredentials());
        afterHandle(userDetails, response);

        return false;
    }

    private void validUserDetails(UserDetails userDetails, String password) {
        isNullUserDetails(userDetails);
        isEqualsPassword(userDetails, password);
    }

    private void isNullUserDetails(UserDetails userDetails) {
        if(userDetails == null) {
            throw new AuthenticationException();
        }
    }

    private void isEqualsPassword(UserDetails userDetails, String password) {
        if(!userDetails.isEqualsPassword(password)) {
            throw new AuthenticationException();
        }
    }

    public abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request);

    public abstract UserDetails getUserDetails(AuthenticationToken authenticationToken);

    public abstract void afterHandle(UserDetails userDetails, HttpServletResponse response);

}
