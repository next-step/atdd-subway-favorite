package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.userdetail.UserDetailService;
import nextstep.auth.userdetail.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

public abstract class IsolationAuthenticationFilter implements HandlerInterceptor {

    private final UserDetailService userDetailService;

    public IsolationAuthenticationFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication auth = getAuth(getToken(request));
        postAuthenticate(auth, response);
        return false;
    }

    public Authentication getAuth(AuthenticationToken token) {
        UserDetails userDetails = findLoginMember(token);
        return new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
    }

    private UserDetails findLoginMember(AuthenticationToken token) {
        UserDetails userDetails = getUserDetails(token);

        if (userDetails.isInvalidCredentials(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return userDetails;
    }

    private UserDetails getUserDetails(AuthenticationToken token) {
        try {
            return userDetailService.loadUserByUsername(token.getPrincipal());
        } catch (NoSuchElementException e) {
            throw new AuthenticationException();
        }
    }
    public abstract AuthenticationToken getToken(HttpServletRequest request);

    protected abstract void postAuthenticate(Authentication authentication, HttpServletResponse response);

}
