package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.UserDetail;
import nextstep.subway.auth.ui.converter.AuthenticationConverter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailService userDetailService;
    private final AuthenticationConverter authenticationConverter;

    public AuthenticationInterceptor(UserDetailService userDetailService, AuthenticationConverter authenticationConverter) {
        this.userDetailService = userDetailService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.converter(request);
        Authentication authentication = authenticate(authenticationToken);

        afterAuthentication(request, response, authentication);
        return false;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetail userDetail = userDetailService.loadUserByUsername(principal);
        checkAuthentication(userDetail, authenticationToken);
        return new Authentication(userDetail);
    }

    private boolean isEqualsPassword(UserDetail userDetail, AuthenticationToken authenticationToken) {
        return userDetail.checkPassword(authenticationToken.getCredentials());
    }

    private void checkAuthentication(UserDetail userDetail, AuthenticationToken authenticationToken) {
        if (userDetail == null) {
            throw new RuntimeException();
        }

        if (!isEqualsPassword(userDetail, authenticationToken)) {
            throw new RuntimeException();
        }
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
