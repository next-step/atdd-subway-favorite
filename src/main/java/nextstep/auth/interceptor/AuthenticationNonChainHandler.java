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
        Object handler) throws Exception {
        AuthenticationToken authenticationToken = getAuthenticationToken(request);

        UserDetails userDetails = getUserDetails(authenticationToken);
        validUserDetails(userDetails, authenticationToken.getCredentials());
        afterHandle(userDetails, response);

        return false;
    }

    private void validUserDetails(UserDetails userDetails, String password) {
        isNullUserDetails(userDetails);
        isValidatePassword(userDetails, password);
    }

    private void isNullUserDetails(UserDetails userDetails) {
        if(userDetails == null) {
            throw new AuthenticationException();
        }
    }

    private void isValidatePassword(UserDetails userDetails, String password) {
        if(userDetails.isValidPassword(password)) {
            throw new AuthenticationException();
        }
    }

    protected abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request);

    protected abstract UserDetails getUserDetails(AuthenticationToken authenticationToken);

    protected abstract void afterHandle(UserDetails userDetails, HttpServletResponse response);

}
