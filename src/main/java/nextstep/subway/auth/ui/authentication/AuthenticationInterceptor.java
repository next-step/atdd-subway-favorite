package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetail;
import nextstep.subway.auth.exception.InvalidPasswordException;
import nextstep.subway.auth.service.UserDetailService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter converter;
    private final UserDetailService userDetailsService;

    public AuthenticationInterceptor(AuthenticationConverter converter, UserDetailService userDetailsService) {
        this.converter = converter;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = converter.convert(request);
        Authentication authenticate = authenticate(authenticationToken);

        afterAuthentication(request, response, authenticate);

        return false;
    }

    protected abstract void afterAuthentication(HttpServletRequest request,
                                             HttpServletResponse response,
                                             Authentication authentication) throws IOException;

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetail userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetail userDetails, AuthenticationToken token) {
        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new InvalidPasswordException();
        }
    }
}
