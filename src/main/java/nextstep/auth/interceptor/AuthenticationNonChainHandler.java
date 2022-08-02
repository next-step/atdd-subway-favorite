package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationNonChainHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        AuthenticationToken authenticationToken = getAuthenticationToken(request);

        UserDetails userDetails = getUserDetails(authenticationToken);
        validUserDetails(userDetails, authenticationToken.getCredentials());
        afterHandle(userDetails, response);
        return false;

    }

    private void validUserDetails(UserDetails userDetails, String password){
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        if (userDetails.isValidPassword(password)) {
            throw new AuthenticationException();
        }
    }

    protected abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request);

    protected abstract UserDetails getUserDetails(AuthenticationToken authenticationToken);

    protected abstract void afterHandle(UserDetails userDetails, HttpServletResponse response);
}
