package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.CustomUserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.exception.InvalidPasswordException;
import nextstep.subway.auth.exception.NoUserFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {
    private CustomUserDetailsService customUserDetailsService;

    public AbstractAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);
        setResponse(request, response, authentication);

        return false;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected abstract void setResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);
        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken authenticationToken) {
        if(userDetails == null) {
            throw new NoUserFoundException();
        }

        if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
            throw new InvalidPasswordException();
        }
    }
}
