package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetail;
import nextstep.subway.auth.domain.UserDetailService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailService userDetailService;
    private final AuthenticationConverter authenticationConverter;

    public AbstractAuthenticationInterceptor(UserDetailService userDetailService, AuthenticationConverter authenticationConverter) {
        this.userDetailService = userDetailService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        if (authentication == null) {
            throw new RuntimeException();
        }

        afterAuthentication(request, response, authentication);
        return false;
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetail userDetail = userDetailService.loadUserByUsername(principal);
        checkAuthentication(userDetail, authenticationToken);

        return new Authentication(userDetail);
    }

    private void checkAuthentication(UserDetail userDetail, AuthenticationToken authenticationToken) {
        if (userDetail == null) {
            throw new RuntimeException();
        }

        if (!userDetail.checkPassword(authenticationToken.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
