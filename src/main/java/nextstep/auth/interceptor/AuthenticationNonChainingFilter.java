package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.domain.NotFoundMemberException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationNonChainingFilter implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;

    public AuthenticationNonChainingFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationToken token = getAuthenticationToken(request);
        Authentication authentication = getAuthentication(token);
        afterAuthentication(authentication, response);
        return false;
    }

    public Authentication getAuthentication(AuthenticationToken token) {
        UserDetails userDetails = findLoginMember(token);
        return new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
    }

    private UserDetails findLoginMember(AuthenticationToken token) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());

            if (userDetails.invalidCredentials(token.getCredentials())) {
                throw new AuthenticationException();
            }

            return userDetails;
        } catch (NotFoundMemberException e) {
            throw new AuthenticationException();
        }
    }

    public abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request);

    protected abstract void afterAuthentication(Authentication authentication, HttpServletResponse response);
}
