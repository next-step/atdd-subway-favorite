package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.converter.AuthenticationConverter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailsService customUserDetailsService;
    private final AuthenticationConverter authenticationConverter;

    protected AuthenticationInterceptor(UserDetailsService customUserDetailsService, AuthenticationConverter authenticationConverter) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        afterAuthentication(request, response, authentication);
        return false;
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        AuthMember userDetails = customUserDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(AuthMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
