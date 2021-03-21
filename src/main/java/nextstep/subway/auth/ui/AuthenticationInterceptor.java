package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationConverter authenticationConverter;

    protected AuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, AuthenticationConverter authenticationConverter) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        afterAuthentication(request, response, authentication);

        return false;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        LoginMember userDetails = customUserDetailsService.loadUserByUsername(principal);
        validateAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void validateAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.validatePassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }

    protected abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
