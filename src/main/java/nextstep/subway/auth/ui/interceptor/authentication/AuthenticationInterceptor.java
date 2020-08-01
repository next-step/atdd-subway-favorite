package nextstep.subway.auth.ui.interceptor.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.subway.auth.application.UserDetail;
import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.interceptor.convert.AuthenticationConverter;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter authenticationConverter;
    private final UserDetailService userDetailService;

    protected AuthenticationInterceptor(AuthenticationConverter authenticationConverter,
        UserDetailService userDetailService) {
        this.authenticationConverter = authenticationConverter;
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        IOException {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token);

        if (authentication == null) {
            throw new RuntimeException();
        }
        afterAuthentication(request, response, authentication);
        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        UserDetail userDetails = userDetailService.loadUserByUserName(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException;

    private void checkAuthentication(UserDetail userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException("there is no user.");
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException("you put wrong password.");
        }
    }
}
