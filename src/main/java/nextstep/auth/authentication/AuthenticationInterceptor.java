package nextstep.auth.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.UserDetailService;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    protected AuthenticationConverter authenticationConverter;
    private final UserDetailService userDetailsService;

    protected AuthenticationInterceptor(UserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);
        afterAuthentication(request, response, authentication);

        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        return authenticationConverter.convert(request);
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        UserDetails loginMember = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(loginMember, token);

        return new Authentication(loginMember);
    }

    public abstract void afterAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException;

    protected void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
