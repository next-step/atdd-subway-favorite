package nextstep.subway.auth.ui;


import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.User;
import nextstep.subway.auth.infrastructure.UserDetailService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailService userDetailsService;
    private final AuthenticationConverter converter;

    protected AuthenticationInterceptor(UserDetailService userDetailService, AuthenticationConverter converter) {
        this.userDetailsService = userDetailService;
        this.converter = converter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = converter.convert(request);
        Authentication authentication = authenticate(token);

        if (authentication == null) {
            throw new RuntimeException();
        }

        afterAuthentication(request, response, authentication);

        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        User user = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(user, token);

        return new Authentication(user);
    }

    private void checkAuthentication(User user, AuthenticationToken token) {
        if (user == null) {
            throw new RuntimeException();
        }

        if (!user.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
