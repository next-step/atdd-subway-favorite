package nextstep.auth.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.user.UserDetailService;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationNonChainInterceptor implements HandlerInterceptor {
    private final UserDetailService userDetailService;

    public AuthenticationNonChainInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        UserInformation userInformation = createPrincipal(request);
        UserDetails userDetails = userDetailService.loadUserByUsername(userInformation.getEmail());

        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(userInformation.getPassword())) {
            throw new AuthenticationException();
        }

        afterAuthentication(response, userDetails);

        return false;
    }

    protected abstract void afterAuthentication(HttpServletResponse response, UserDetails userDetails)
            throws IOException;

    protected abstract UserInformation createPrincipal(HttpServletRequest request) throws IOException;
}
