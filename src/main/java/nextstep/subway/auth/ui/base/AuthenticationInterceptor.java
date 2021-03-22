package nextstep.subway.auth.ui.base;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.base.AuthenticationConverter;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.exception.InvalidCredentialsException;
import nextstep.subway.auth.exception.NotFoundUserException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter authenticationConverter;

    public AuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token);

        afterAuthentication(request, response, authentication);
        return false;
    }

    public abstract void afterAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException;

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new NotFoundUserException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new InvalidCredentialsException();
        }
    }
}
